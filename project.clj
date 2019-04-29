(defproject evalback "0.2.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.12.5"]]
  :java-source-paths ["src/java"]
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [compojure "1.6.1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.4.490"]
                 [de.hhu.stups/de.prob2.kernel "3.2.3"]
                 [hiccup "1.0.5"]
                 [ring/ring-core "1.7.1"]
                 ]
  :main evalback.core
  :ring {:handler evalback.core/handler
         :init evalback.core/init
         :destroy evalback.core/destroy
         :uberwar-name "evalB.war"}
  :aot :all)
