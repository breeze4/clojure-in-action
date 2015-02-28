(ns cia.chapter08.expense-finders-spec
  (:use cia.chapter08.expense-finders)
  (:use cia.chapter08.stubbing)
  (:use clojure.test))

(def all-expenses [(struct-map expense :amount 10.0 :date "2010-02-28")
                   (struct-map expense :amount 20.0 :date "2010-02-25")
                   (struct-map expense :amount 30.0 :date "2010-02-21")])

;;(stubbing [function-name1 stubbed-return-value1
;;  function-name2 stubbed-return-value2...]
;;  code-body)

;; (deftest test-fetch-expenses-greater-than
;;   (stubbing [fetch-all-expenses all-expenses]
;;             (let [filtered (fetch-expenses-greater-than "" "" "" 15.0)]
;;               (is (= (count filtered) 2))
;;               (is (= (:amount (first filtered)) 20.0))
;;               (is (= (:amount (last filtered)) 30.0)))))

;; (deftest test-filter-greater-than
;;   (let [fetched [(struct-map expense :amount 10.0 :date "2010-02-28")
;;                  (struct-map expense :amount 20.0 :date "2010-02-25")
;;                  (struct-map expense :amount 30.0 :date "2010-02-21")]
;;         filtered (expenses-greater-than fetched 15.0)]
;;     (is (= (count filtered) 2))
;;     (is (= (:amount (first filtered)) 20.0))
;;     (is (= (:amount (last filtered)) 30.0))))

;; above implementations both log via the log-call function
(deftest test-filter-greater-than-2
  (mocking [log-call]
           (let [filtered (expenses-greater-than all-expenses 15.0)]
             (is (= (count filtered) 2))
             (is (= (:amount (first filtered)) 20.0))
             (is (= (:amount (last filtered)) 30.0)))
           (verify-call-times-for log-call 1)
           (verify-first-call-args-for log-call "expenses-greater-than" 15.0)
           (verify-nth-call-args-for 1 log-call "expenses-greater-than" 15.0)))

;; with defmocktest macro
(defmocktest test-fetch-expenses-greater-than
  (stubbing [fetch-all-expenses all-expenses]
            (let [filtered (fetch-expenses-greater-than "" "" "" 15.0)]
              (is (= (count filtered) 2))
              (is (= (:amount (first filtered)) 20.0))
              (is (= (:amount (last filtered)) 30.0)))))

(defmocktest test-filter-greater-than
  (mocking [log-call]
           (let [filtered (expenses-greater-than all-expenses 15.0)]
             (is (= (count filtered) 2))
             (is (= (:amount (first filtered)) 20.0))
             (is (= (:amount (last filtered)) 30.0)))
           (verify-call-times-for log-call 1)
           (verify-first-call-args-for log-call "expenses-greater-than" 15.0)))

;; refactored to include testing macro
(defmocktest test-filter-greater-than
             (mocking [log-call]
                      (let [filtered (expenses-greater-than all-expenses 15.0)]
                        (testing "the filtering itself works as expected"
                          (is (= (count filtered) 2))
                          (is (= (:amount (first filtered)) 20.0))
                          (is (= (:amount (last filtered)) 30.0))))
                      (testing "Auditing via log-call works correctly"
                        (verify-call-times-for log-call 1)
                        (verify-first-call-args-for log-call "expenses-greater-than" 15.0))))


;; experimentation below:
;; (defn ^:dynamic calc-x [x1 x2]
;;   (* x1 x2))

;; (defn ^:dynamic calc-y [y1 y2]
;;   (/ y2 y1))

;; (defn some-client []
;;   (println (calc-x 2 3) (calc-y 3 4)))

;; (macroexpand-1 '(stubbing [calc-x 1
;;                            calc-y 2]
;;                           (some-client)))

;; (some-client)

;; (stubbing [calc-x 1
;;            calc-y 2]
;;           (some-client))

;; are macro example usage:
;(defn to-upper [s]
;  (.toUpperCase (str s)))

;(deftest test-to-upcase
;  (is (= "RATHORE" (to-upper "rathore")))
;  (is (= "1" (to-upper 1)))
;  (is (= "AMIT" (to-upper "AMIT"))))

;(deftest test-to-upcase
;  (are [l u] (= u (to-upper l))
;             "RATHORE" "RATHORE"
;             "1" "1"
;             "amit" "AMIT"))