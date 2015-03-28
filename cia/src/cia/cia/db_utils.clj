(ns cia.db-utils
  (:require
    [cia.macro-learnings :refer :all]
    [clojure.java.jdbc :as sql]))

(def db-spec
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "mem:cia"
   :user        ""
   :password    ""})

; general idea:
; use threading macro for: (slurp file) (split ";" _) (sql/db-do-prepared db-spec _)
; -> and ->> only worked with wrapping db-do-prepared to switch the placement of the parameter

; first version, before discovering db-do-prepared doesn't
; support multiple SQL statements in a file
(defn- exec-sql-file-original
  [file]
  (sql/db-do-prepared db-spec (slurp file)))

; works for multiple SQL statements, kinda ugly
(defn- exec-sql-file-alt-2
  [file]
  (map #(sql/db-do-prepared db-spec %)
       (.split (slurp file) ";\r\n")))

; cleanest version and it is the easiest to read
; with an arbitrary parameter position threading macro
(defn exec-sql-file [file]
  "Reads in the script file and splits into statements to be executed individually in order
 Splits on \";\r\n\" which is probably dumb and incompatible on other OSes than Windows :)"
  (->>> (slurp file)
        (.split _ ";\r\n")
        (map #(sql/db-do-prepared db-spec %) _)))

; let form isn't so bad either though
; let is more idiomatic, but I like threading macros!
(defn- exec-sql-file-idiomatic [file]
  "Reads in the script file and splits into statements to be executed individually in order
  Splits on \";\r\n\" which is probably dumb and incompatible on other OSes than Windows :)"
  (let [batch (slurp file)
        stmts (.split batch ";\r\n")]
    (map #(sql/db-do-prepared db-spec %) stmts)))

; just learned this as->
(defn exec-sql-file-as [file]
  "Reads in the script file and splits into statements to be executed individually in order
 Splits on \";\r\n\" which is probably dumb and incompatible on other OSes than Windows :)"
  (as-> (slurp file) $
        (.split $ ";\r\n")
        (map #(sql/db-do-prepared db-spec %) $)))
