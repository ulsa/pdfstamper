(defproject pdfstamper "0.1.0-SNAPSHOT"
  :description "Stamps PDF files with their names"
  :url "https://github.com/ulsa/pdfstamper"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repl-options {:init (do
                         (require '[clojure.repl :refer :all]
                                  '[clojure.pprint :refer :all]))}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.4"]
                 [org.apache.pdfbox/pdfbox-app "1.8.2"]]
  :main pdfstamper.core
  :profiles {:uberjar {:aot :all}})
