(ns cia.chapter08.expense-finders
  (:require [clojure.string :as str]))

(defstruct ^:dynamic expense :amount :date)

(defn ^:dynamic log-call [id & args]
  (println "Audit - called" id "with:" (str/join ", " args))
  ;;do logging to some audit data-store
  )

(defn ^:dynamic fetch-all-expenses [username start-date end-date]
  (log-call "fetch-all" username start-date end-date)
  ;find in data-store, return list of expense structs
  )

(defn expenses-greater-than [expenses threshold]
  (log-call "expenses-greater-than" threshold)
  (filter #(> (:amount %) threshold) expenses))

(defn fetch-expenses-greater-than [username start-date end-date threshold]
  (let [all (fetch-all-expenses username start-date end-date)]
    (expenses-greater-than all threshold)))
