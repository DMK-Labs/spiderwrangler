(ns spiderwrangler.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.jdbc :as jdbc]
            [semantic-csv.core :as sem-csv]
            [taoensso.nippy :as nippy]
            [clojure.java.io :as io]
            [aero.core :refer (read-config)]))

(def config (read-config "config.edn")) ; Project root에 config.edn파일이 있으나 이 파일은
                                        ; ~/.secrets.edn에서 실제 정보를 읽어옴

;; DB 연결 정보
(let [{:keys [subname user password]} config]
  (def db {:classname "com.mysql.jdbc.Driver"
           :subprotocol "mysql"
           :subname subname
           :user user
           :password password}))

;; DB 내 Table 값을 모두 vector of maps로 읽어드림:
;; [{:a 1 :foo bar} {:a 3 :foo baz}]
(def crawl-board
  ;; db에 SQL query 던져서 받은 값
  (jdbc/query db ["SELECT board_id, author, title, category, url, registered_date FROM crawl_board"]))

(def crawl-clicks
  (jdbc/query db ["SELECT board_id, date, clicks FROM crawl_board_clicks"]))

(cons 1 [2 3 4 5])
;; => (1 2 3 4 5)

(defn get-coords
  "For a given crawled item from 'crawl-board':
  1. Get all clicks entries with the same :board_id from 'crawl-clicks'
  2. Prepend the zeroeth entry"
  [crawled-item]
  (let [id (:board_id crawled-item)
        start-time (.getTime (:registered_date crawled-item))]
    ;; TODO: Use transducers
    (->> (filter #(= id (:board_id %)) crawl-clicks)
         (map (fn [m] (update m :date #(.getTime %))))
         (cons {:board_id id :date start-time :clicks 0}))))

(defn get-coords2
  "For a given crawled item from 'crawl-board':
  1. Get all clicks entries with the same :board_id from 'crawl-clicks'
  2. Prepend the zeroeth entry"
  [crawled-item]
  (let [id (:board_id crawled-item)
        start-time (.getTime (:registered_date crawled-item))]
    (cons {:board_id id :date start-time :clicks 0}
          (sequence (comp (filter #(= id (:board_id %)))
                          (map (fn [m] (update m :date #(.getTime %)))))
                    crawl-clicks))))

(comment ;; 위 함수를 전체 crawl-board에 map 한 결과를 파일로 임시 저장한다
  (time
   (nippy/freeze-to-file "data/coordinates" (flatten (pmap get-coords crawl-board))))
  "Elapsed time: 51924.316082 msecs")

(comment ;; Using transducers
  (time
   (nippy/freeze-to-file "data/coordinates" (flatten (pmap get-coords2 crawl-board))))
  "Elapsed time: 23477.693297 msecs")

(with-open [o (io/writer "data/coords.csv")]                ; 새로 쓸 CSV
  (let [data (nippy/thaw-from-file "data/coordinates")] ; 아까 임시 저장 데이터
    (->> (sem-csv/vectorize data)    ; vector of maps -> vector of strings csv로 쓰기 위해
         (csv/write-csv o))))
