(ns cia.chapter09.user
  (:require [korma.db :as db]
            [korma.core :refer :all]
            [clojure.java.jdbc :as sql]
            [cia.db-utils :refer :all]))

(exec-sql-file "src/cia/chapter09/drop-tables.sql")
(exec-sql-file "src/cia/chapter09/users.sql")

(db/defdb db-mem (db/sqlite3 db-spec))

(defentity users
           (database db-mem))

(defentity charges
           (belongs-to users))

(defn add-user [u] (insert users (values u)))
(defn add-charge [c] (insert charges (values c)))

; add some sample data to exercise it
(add-user {:login         "rob"
           :first_name    "Robert"
           :last_name     "Berger"
           :password      "secret"
           :email_address "rob@runa.com"})

(add-user {:login         "rob2"
           :first_name    "Robert"
           :last_name     "Stevenson"
           :password      "friday"
           :email_address "rob@crusoe.com"})

(add-charge {:user_id        1
             :amount_dollars 50
             :amount_cents   23
             :category       "financial"
             :vendor_name    "vanguard"})

(select users)
;(select users (where {:id 2}))

;(update users
;        (set-fields {:login "stevenson" :id 5})
;        (where {:id 2}))

;(delete users (where {:id 5}))
;;