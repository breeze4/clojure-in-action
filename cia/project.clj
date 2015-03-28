(defproject cia "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [org.xerial/sqlite-jdbc "3.8.7"]
                 [korma "0.4.0"]
                 [org.apache.hbase/hbase "0.94.26"]
                 [org.apache.hadoop/hadoop-core "1.2.1"]
                 [com.taoensso/carmine "2.9.0"]
                 [com.rabbitmq/amqp-client "3.5.0"]
                 ]
  :plugins [[com.jakemccrary/lein-test-refresh "0.6.0"]])
