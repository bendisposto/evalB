(ns evalback.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]
            [hiccup.core :as h]
            [hiccup.page :as hp]
            [hiccup.element :as he]
            [clojure.data.json :as json]
            [clojure.core.async :as a :refer [go chan <!! >!!]])
  (:use ring.server.standalone
        [ring.middleware file-info file])
  (:import de.prob.animator.command.CbcSolveCommand
           de.prob.unicode.UnicodeTranslator
           de.tla2b.exceptions.TLA2BException
                                        ;de.tla2b.translation.ExpressionTranslator
           ))

(def instances 2)
(defonce server (atom nil))
(defonce worker (atom nil))

(defmulti process-result (fn [r _] (class r)))
(defmethod process-result EvalResult [res resp]
  (into resp {:status :ok
              :result (.getValue res)
              :bindings (into {} (.getSolutions res))}))
(defmethod process-result ComputationNotCompletedResult [res resp]
  (into resp {:status :error
              :result (.getReason res)}))


(defmulti run-eval :formalism)
(defmethod run-eval :b [ss {:keys [input] :as resp}]
  (let [c (CbcSolveCommand. (ClassicalB. input))]
    (try (do
           (.execute ss c)
           (process-result (.getValue c) resp))
         (catch Exception e
           (into resp {:status :error
                       :result (.getMessage e)})))))


(defn solve [request]
  (let [solver (<!! @worker)
        result (solver request)]
    (>!! @worker solver)
    result))



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


(defroutes app
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

(defn mk-worker [animator]
  (>!! @worker
       (fn [request]
         (let [result-future (future (run-eval animator request))
               result (deref result-future 3000 (into request {:result :error :result "Timeout"}))]
           (future-cancel result-future)
           result))))

(defn init []
  (reset! worker (chan instances))
  (let [tf (java.io.File/createTempFile "evalb" ".mch" nil)
        tn (.getAbsolutePath tf)
        api (.getInstance (Main/getInjector) Api)]
    (.deleteOnExit tf)
    (spit tf "MACHINE empty \n END")
    (doseq [_ (range instances)]
      (mk-worker (.. (.b_load api tn) getStateSpace))))
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
      (wrap-file-info)))

(defn start-server
  "used for starting the server in development mode from REPL"
  [& [port]]
  (let [port (if port (Integer/parseInt port) 3000)]
    (reset! server
            (serve (get-handler)
                   {:port port
                    :init init
                    :auto-reload? true
                    :destroy destroy
                    :join? false}))
    (println (str "You can view the site at http://localhost:" port))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))
