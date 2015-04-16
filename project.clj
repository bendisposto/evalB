(defproject evalback "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["cobra" "http://cobra.cs.uni-duesseldorf.de/artifactory/repo"]]
  :plugins [[lein-ring "0.8.11"]]
  :java-source-paths ["src/java"]
  :ring {:handler liberator-tutorial.core/handler}
  :dependencies [[org.clojure/clojure "1.7.0-alpha4"]
                 [liberator "0.10.0"]
                 [compojure "1.1.3"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [de.prob2/de.prob2.kernel "2.0.0-milestone-24-SNAPSHOT"]
                 [hiccup "1.0.5"]
                 [ring/ring-core "1.2.1"]
                 [ring-server "0.3.1"]]
  :main evalback.core)
