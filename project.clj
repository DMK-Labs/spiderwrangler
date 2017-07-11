(defproject
  spiderwrangler
  "0.1.0-SNAPSHOT"
  :repositories
  [["clojars" {:url "https://repo.clojars.org/"}]
   ["maven-central" {:url "https://repo1.maven.org/maven2"}]]
  :dependencies
  [[org.clojure/clojure "1.9.0-alpha14"]
   [adzerk/boot-test "1.2.0" :scope "test"]
   [org.clojure/java.jdbc "0.4.2"]
   [mysql/mysql-connector-java "5.1.6"]
   [huri "0.10.0-20170616.110743-7"]
   [com.taoensso/nippy "2.13.0"]
   [semantic-csv "0.2.1-alpha1"]
   [org.clojure/data.csv "0.1.4"]
   [aero "1.1.2"]
   [onetom/boot-lein-generate "0.1.3"]]
  :source-paths
  ["test"]
  :resource-paths
  ["src" "resources"])