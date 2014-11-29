(ns evalback.exp
  (:require [clojure.core.async :as a :refer [go chan <!! >!!]])
  (:import de.prob.animator.IAnimator))

(def instances 2)
(defonce worker (atom (chan instances)))

(defn run-eval [animator request]
  (let [sp (+ (rand-int 2000) 2000)]
                             (Thread/sleep sp)
                             
                             (println "solve" request "on" (.getId animator) "in" sp)
                             {:result :ok}))

(defn mk-worker []
  (let [animator (.getInstance (de.prob.Main/getInjector) IAnimator)]
    (>!! @worker
         (fn [request]
           (let [result-future (future (run-eval animator request))
                 result (deref result-future 3000 {})]
             (future-cancel result-future)
             result)))))

(defn solve [request]
  (let [solver (<!! @worker)
        result (solver request)]
    (>!! @worker solver)
    result))
