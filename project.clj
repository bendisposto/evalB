(defproject evalback "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.9.3"]]
  :java-source-paths ["src/java"]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.1.3"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [de.hhu.stups/de.prob2.kernel "2.0.0"]
                 [hiccup "1.0.5"]
                 [ring/ring-core "1.2.1"]
                 [ring-server "0.3.1"]]
  :main evalback.core
  :ring {:handler evalback.core/handler
         :init evalback.core/init
         ;; :nrepl {:start? true :port 6000}
         :destroy evalback.core/destroy
         :uberwar-name "evalB.war"}
  :aot :all)
