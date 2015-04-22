(ns evalback.core
  (:gen-class :implements [org.apache.commons.daemon.Daemon])
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY GET POST]]
            [hiccup.core :as h]
            [hiccup.page :as hp]
            [hiccup.element :as he]
            [clojure.data.json :as json]
            [clojure.core.async :as a :refer [alts!! chan <!! >!! timeout]])
  (:use ring.server.standalone
        [ring.middleware file-info file])
  (:import [org.apache.commons.daemon Daemon DaemonContext]
           de.prob.animator.command.CbcSolveCommand
           (de.prob.animator.domainobjects ClassicalB TLA EvalResult ComputationNotCompletedResult)
           de.prob.unicode.UnicodeTranslator
           de.tla2b.exceptions.TLA2BException
           de.prob.Main
           de.prob.scripting.Api
           (de.be4.classicalb.core.parser.node TIdentifierLiteral AImplicationPredicate)))

(def varname [(TIdentifierLiteral. "_lambda_result_")])

(def instances 2)
(def prob-timeout 3000)
(def request-timeout 6500)
(defonce server (atom nil))
(defonce worker (atom nil))

(defmulti process-result (fn [r _ _ _] (class r)))
(defmethod process-result EvalResult [res cbf introduced resp]
  (let [result (.getValue res)
        bindings (into {} (.getSolutions res))]
    (into resp {:status :ok
                :input cbf
                :introduced introduced
                :result result
                :bindings bindings})))

(defmethod process-result ComputationNotCompletedResult [res cbf _ resp]
  (let [reason (.getReason res)]
    (condp = reason
      "contradiction found" (into resp {:status :ok :result false :input cbf})
      (into resp {:status :error :result reason}))))

(defmulti instantiate (fn [f i] f))

(defmethod instantiate :b [_ input] (ClassicalB. input))
(defmethod instantiate :tla [_ input] (TLA. input))

(defn mk-formula [formalism input]
  (let [cbf (instantiate formalism input)
        pred? (= "#PREDICATE" (.getKind cbf))]
    (if pred?
      [cbf nil]
      (let [
            cbf (instantiate formalism (str  "lambda_result_ = " input))
            ast (.. cbf getAst getPParseUnit getPredicate getLeft (setIdentifier varname))]
        [cbf "_lambda_result_"]))))


(defn top-level-implication? [cbf]
  (let [ast (.getAst cbf)
        pu (.getPParseUnit ast)
        pred (.getPredicate pu)]
    (if (instance? AImplicationPredicate pred)
      "\nYou use an implication at the top level. This is most likely not what you meant. Remember that free variables are existentially quantified.\n\n"
      "")))



(defn run-eval  [ss {:keys [input formalism] :as resp}]
  (try (let [[cbf introduced] (mk-formula formalism input)
             c (CbcSolveCommand. cbf)]
         (.execute ss c)
         (process-result (.getValue c) cbf introduced resp))
       (catch Exception e
         (println e)
         (into resp {:status :error
                     :result (.getMessage e)}))))


(defn solve [request]
  (let [[solver _] (alts!! [@worker (timeout request-timeout)])]
    (if solver
      (let [result (solver request)]
        (>!! @worker solver)
        result)
      (into request {:status :error :result "The system is under heavy load. Please try again later."}))))


(defn unicode [s]
  (UnicodeTranslator/toUnicode s))

(defn html-result [formula {:keys [status result bindings]}]
  (h/html
   [:html
    [:head
     [:title "evalB"]
     (hp/include-css "/style.css")]
    [:body {:class (name status)}
     [:h1 "ProB 2.0 - evalB"]
     [:h2 "Input:"]
     [:p (unicode formula)]
     [:h2 "Result:"]
     [:p (if (= :ok status) (unicode result) result)]
     [:h2 "Solutions"]
     (he/unordered-list (map (fn [[k v]] (str k " = " (unicode v))) bindings))]]))

(defn edn-result [formula res]
  (assoc res :input formula))

(defn json-result [formula res]
  (json/write-str (assoc res :input formula)))

(defn valid-reply [result input introduced bindings]
  (if introduced
    (apply str (get bindings introduced "No solution computed.")
           (if (< 1 (count bindings)) "\n\nSolution: \n" "")
           (for [[k v] bindings] (if-not (= k introduced) (str "  " k "=" v "\n") "")))
    (apply str "Predicate is " (if result "satisfiable" "not satisfiable") ".\n"
           (top-level-implication? input)
           (if (seq bindings) "\nSolution: \n" "")
           (for [[k v] bindings] (str "  " k "=" v "\n")))))

(defn old-json [{:keys [status result input introduced  bindings]}]
  (json/write-str
   (if (= status :error)
     {:output (str "Error: " result)}
     {:output (valid-reply result input introduced bindings)})))

(defn get-api [] (.getInstance (Main/getInjector) Api))

(defroutes app
  (ANY "/version" [] (str (.getVersion (get-api))))


  (POST "/xxx" [formalism input]
        (let [r (solve {:formalism  (keyword formalism)
                        :input input })]
          (old-json r)))

  (ANY "/eval/:formalism/:formula" [formalism formula]
       (resource :available-media-types ["text/html" "application/clojure" "application/json"]
                 :handle-ok
                 (fn [context]
                   (let [r (solve {:formalism  (keyword formalism) :input formula})]
                     (condp =
                         (get-in context [:representation :media-type])
                       "text/html" (html-result formula r)
                       "application/clojure" (edn-result formula r)
                       "application/json" (json-result formula r)))))))

(def handler
  (-> app
      wrap-params))


(defn mk-worker [tn]
  (let [animator (.. (.b_load (get-api) tn) getStateSpace)]
    (fn [request]
      (assoc (let [result-future (future (run-eval animator request))
                   result (deref
                           result-future
                           prob-timeout
                           (into request {:status :error :result "Timeout"}))]
               (future-cancel result-future)
               result)
             :animator-id (.getId animator)))))

(defn create-empty-machine []
  (let [tf (java.io.File/createTempFile "evalb" ".mch" nil)
        tn (.getAbsolutePath tf)
        ]
    (.deleteOnExit tf)
    (spit tf "MACHINE empty \n END")
    tn))

(defn init []
  (reset! worker (chan instances))
  (let [tn (create-empty-machine)]
    (doseq [_ (range instances)]
      (>!! @worker (mk-worker tn)))
    )
  (println :init))

(defn destroy []
  (doseq [_ (range instances)] (<!! @worker))
  (reset! worker nil)
  (println :destroy))

(defn get-handler []
  ;; #'app expands to (var app) so that when we reload our code,
  ;; the server is forced to re-resolve the symbol in the var
  ;; rather than having its own copy. When the root binding
  ;; changes, the server picks it up without having to restart.
  (-> #'app
                                        ; Makes static assets in $PROJECT_DIR/resources/public/ available.
      (wrap-file "resources")
                                        ; Content-Type, Content-Length, and Last Modified headers for files in body
      (wrap-file-info)
      wrap-params))

(def port (let [p (System/getProperty "port")]
            (if p (Integer/parseInt p) 9000)))

(defn start-server
  "used for starting the server in development mode from REPL"
  []
  (reset! server
          (serve (get-handler)
                 {:port port
                  :init init
                  :open-browser? false
                  :stacktraces? false
                  :auto-reload? false
                  :destroy destroy
                  :join? true})))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

(defn -init [this ^DaemonContext context])

(defn -start [this]
  (future (start-server)))

(defn -stop [this]
  (stop-server))

(defn -main [& args]
  (start-server))
