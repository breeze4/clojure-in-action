(ns cia.ch5)

;; java interop
(import '(java.text SimpleDateFormat))

(def sdf (new SimpleDateFormat "yyyy-MM-dd"))
;; equivalent with . syntax
(def sdf (SimpleDateFormat. "yyyy-MM-dd"))

;; example of syntactic sugar, converting this "call toString in the scope of sdf with <> args passed":
(. sdf toString)
;; to a more idiomatic form "call toString on sdf with <> args":
(.toString sdf)

(defn date-from-date-string [date-string]
  (let [sdf (SimpleDateFormat. "yyyy-MM-dd")]
    (.parse sdf date-string)))

(date-from-date-string "2015-06-01")

;; static methods:
(Long/parseLong "12321")

(import '(java.util Calendar))
Calendar/JANUARY
Calendar/FEBRUARY
(. Calendar DECEMBER)

;; (. System getenv "PATH")
;; (. System (getenv "PATH"))

(import '(java.util Random))
(def rnd (Random. ))
(. rnd nextInt 10)
(. rnd (nextInt 10))
(.nextInt rnd 10)

(import '(java.util Calendar TimeZone))

(. (. (Calendar/getInstance) (getTimeZone)) (getDisplayName))

(. (. (Calendar/getInstance) getTimeZone) getDisplayName)

(.. (Calendar/getInstance) (getTimeZone) (getDisplayName))

(.. (Calendar/getInstance) getTimeZone getDisplayName)

(..
 (Calendar/getInstance)
 (getTimeZone)
 (getDisplayName true TimeZone/SHORT))

(import '(java.util Calendar))
(defn the-past-midnight-1 []
  (let [calendar-obj (Calendar/getInstance)]
    (.set calendar-obj Calendar/AM_PM Calendar/AM)
    (.set calendar-obj Calendar/HOUR 0)
    (.set calendar-obj Calendar/MINUTE 0)
    (.set calendar-obj Calendar/SECOND 0)
    (.set calendar-obj Calendar/MILLISECOND 0)
    (.getTime calendar-obj)))

;; written with doto macro
(defn the-past-midnight-2 []
  (let [calendar-obj (Calendar/getInstance)]
    (doto calendar-obj
      (.set Calendar/AM_PM Calendar/AM)
      (.set Calendar/HOUR 0)
      (.set Calendar/MINUTE 0)
      (.set Calendar/SECOND 0)
      (.set Calendar/MILLISECOND 0))
    (.getTime calendar-obj)))

(the-past-midnight-1)
(the-past-midnight-2)






































