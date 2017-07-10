(def project 'spiderwrangler)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "1.9.0-alpha14"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [org.clojure/java.jdbc "0.4.2"]
                            [mysql/mysql-connector-java "5.1.6"]
                            [huri "0.10.0-SNAPSHOT"]
                            [com.taoensso/nippy "2.13.0"]
                            [semantic-csv "0.2.1-alpha1"]
                            [org.clojure/data.csv "0.1.4"]
                            [aero "1.1.2"]
                            [onetom/boot-lein-generate "0.1.3"]])

(require '[boot.lein :as lein])

(task-options!
 pom {:project     project
      :version     version
      :description "FIXME: write description"
      :url         "http://example/FIXME"
      :scm         {:url "https://github.com/yourname/spiderwrangler"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build
  "Build and install the project locally."
  []
  (comp (pom) (jar) (install)))

(require '[adzerk.boot-test :refer [test]])
