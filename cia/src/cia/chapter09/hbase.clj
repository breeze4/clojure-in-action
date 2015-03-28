(ns cia.chapter09.hbase
  (:import (java.util Set)
           (org.apache.hadoop.hbase HBaseConfiguration)
           (org.apache.hadoop.hbase.client Put Get HTable)
           (org.apache.hadoop.hbase.util Bytes)))

(defn hbase-table [table-name]
  (HTable. (HBaseConfiguration/create) table-name))

(defn add-to-put [p object column-family]
  (let [name-of (fn [x]
                  (if (keyword? x) (name x) (str x)))]
    (doseq [[k v] object]
      (.add p (Bytes/toBytes column-family)
            (Bytes/toBytes (name-of k))
            (Bytes/toBytes (str v))))))

(defn put-in-table [object table-name column-family row-id]
  (let [table (hbase-table table-name)
        p (Put. (Bytes/toBytes row-id))]
    (add-to-put p object column-family)
    (.put table p)))

;;