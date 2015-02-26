(ns cia.chapter08.date-operations
  (:import (java.text SimpleDateFormat)
           (java.util GregorianCalendar)
           (java.util Calendar)
           (java.lang String)))

(defn date [date-string]
  (let [f (SimpleDateFormat. "yyyy-MM-dd")
        d (.parse f date-string)]
    (doto (GregorianCalendar.)
      (.setTime d))))

(defn month-from [d] ;; offset by 1 :P
  (inc (.get d Calendar/MONTH)))

(defn day-from [d]
  (.get d Calendar/DAY_OF_MONTH))

(defn year-from [d]
  (.get d Calendar/YEAR))

(defn as-string [d]
  (let [yr (year-from d)
        mth (format "%02d" (month-from d))
        day (format "%02d" (day-from d))]
    (str yr "-" mth "-" day)))

;; (def today (date "2009-01-22"))
;; today
;; (.add today Calendar/DAY_OF_MONTH 1) ;; returns nil.. so..
;; today
;; key: use doto for this! duh!
;; naive implementation - need to return a new copy of the date
;; (defn increment-day [d]
;;   (doto d (.add Calendar/DAY_OF_MONTH 1)))

;; (defn increment-month [d]
;;   (doto d (.add Calendar/MONTH 1)))

;; (defn increment-year [d]
;;   (doto d (.add Calendar/YEAR 1)))

;; better, now with factoring out the clone-and-mutate behavior:
(defn notch-date [date field notch]
  (doto (.clone date)
    (.add field notch)))

(defn increment-day [d]
  (notch-date d Calendar/DAY_OF_MONTH 1))

(defn increment-month [d]
  (notch-date d Calendar/MONTH 1))

(defn increment-year [d]
  (notch-date d  Calendar/YEAR 1))

;; decrement
(defn decrement-day [d]
  (notch-date d Calendar/DAY_OF_MONTH -1))

(defn decrement-month [d]
  (notch-date d Calendar/MONTH -1))

(defn decrement-year [d]
  (notch-date d Calendar/YEAR -1))

;; alternate refactoring proposed by book:
(defn date-operator [operation field]
  (fn [d]
    (doto (.clone d)
      (.add field (operation 1)))))
(def increment-day (date-operator + Calendar/DAY_OF_MONTH))
(def increment-month (date-operator + Calendar/MONTH))
(def increment-year (date-operator + Calendar/YEAR))
(def decrement-day (date-operator - Calendar/DAY_OF_MONTH))
(def decrement-month (date-operator - Calendar/MONTH))
(def decrement-year (date-operator - Calendar/YEAR))


