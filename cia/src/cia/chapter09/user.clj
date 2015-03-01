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

(has-many users charges)

(defn add-user [u] (insert users (values u)))
(defn add-charge [c] (insert charges (values c)))

(defn validate-user [user]
                    "Returns the valid user or nothing."
                    (if-let [email (:email_address user)]
                      user))

(defn create-user [user]
  (-> user
      validate-user
      add-user))

(defn validate-charge [charge]
  "Returns the charge if it is a positive amount."
  (if-let [total-amount (+ (* 100 (:amount_dollars charge))
                           (:amount_cents charge))]
    (if (> total-amount 0)
      charge)))

(defn create-charge [charge]
  (-> charge
      validate-charge
      add-charge))

; add some sample data to exercise it
(create-user {:login         "rob"
         :first_name    "Robert"
         :last_name     "Berger"
         :password      "secret"
         :email_address "rob@runa.com"})

(create-user {:login         "rob2"
         :first_name    "Robert"
         :last_name     "Stevenson"
         :password      "friday"
         :email_address "rob@crusoe.com"})

(create-charge {:user_id        1
             :amount_dollars 50
             :amount_cents   23
             :category       "financial"
             :vendor_name    "vanguard"})

(create-charge {:user_id        1
             :amount_dollars 34
             :amount_cents   15
             :category       "ice cream"
             :vendor_name    "the soda shoppe"})

(select users)
;(select users (where {:id 2}))
(select charges (where {:user_id 1}))
;(update users
;        (set-fields {:login "stevenson" :id 5})
;        (where {:id 2}))

;(delete users (where {:id 5}))