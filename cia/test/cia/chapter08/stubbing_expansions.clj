;(ns cia.chapter08.stubbing-expansions
;  (:use cia.chapter08.expense-finders)
;  (:use clojure.test))
;
;(def all-expenses [(struct-map expense :amount 10.0 :date "2010-02-28")
;                   (struct-map expense :amount 20.0 :date "2010-02-25")
;                   (struct-map expense :amount 30.0 :date "2010-02-21")])
;
;(def ^:dynamic mock-calls)
;(def ^:dynamic mock-calls (atom {}))
;
;(defn stub-fn [return-value]
;  (fn [& args]
;    return-value))
;
;;;(stubbing [function-name1 stubbed-return-value1
;;;  function-name2 stubbed-return-value2...]
;;;  code-body)
;
;(defmacro stubbing-1 [stub-forms & body]
;  (let [stub-pairs (partition 2 stub-forms)
;        returns (map last stub-pairs)
;        stub-fns (map #(list 'constantly %) returns)
;        real-fns (map first stub-pairs)]
;    `(binding [~@(interleave real-fns stub-fns)]
;       ~@body)))
;
;(deftest test-fetch-expenses-greater-than
;  (stubbing-1 [fetch-all-expenses all-expenses]
;              (let [filtered (fetch-expenses-greater-than "" "" "" 15.0)]
;                (is (= (count filtered) 2))
;                (is (= (:amount (first filtered)) 20.0))
;                (is (= (:amount (last filtered)) 30.0)))))
;
;;; stubbing-1
;'(clojure.core/binding
;   [fetch-all-expenses (constantly all-expenses)]
;   (let [filtered (fetch-expenses-greater-than "" "" "" 15.0)]
;     (is (= (count filtered) 2))
;     (is (= (:amount (first filtered)) 20.0))
;     (is (= (:amount (last filtered)) 30.0))))
;
;(defn stub-fn [the-function return-value]
;  (swap! mock-calls assoc the-function [])
;  (fn [& args]
;    (swap! mock-calls update-in [the-function] conj args)
;    return-value))
;
;(defmacro stubbing-2 [stub-forms & body]
;  (let [stub-pairs (partition 2 stub-forms)
;        returns (map last stub-pairs)
;        stub-fns (map #(list 'stub-fn %) returns)
;        real-fns (map first stub-pairs)]
;    `(binding [~@(interleave real-fns stub-fns)]
;       ~@body)))
;
;;; stubbing-2
;'(clojure.core/binding
;   [fetch-all-expenses (stub-fn fetch-all-expenses all-expenses)]
;   (let [filtered (fetch-expenses-greater-than "" "" "" 15.0)]
;     (is (= (count filtered) 2))
;     (is (= (:amount (first filtered)) 20.0))
;     (is (= (:amount (last filtered)) 30.0))))
;
;;; stubbing with defmocktest macro, clears state each time
;(defmacro defmocktest-1 [test-name & body]
;  `(deftest ~test-name
;     (binding [mock-calls (atom {})]
;       (do ~@body))))
;
;(defmocktest-1 test-fetch-expenses-greater-than
;               (stubbing-2 [fetch-all-expenses all-expenses]
;                           (let [filtered (fetch-expenses-greater-than "" "" "" 15.0)]
;                             (is (= (count filtered) 2))
;                             (is (= (:amount (first filtered)) 20.0))
;                             (is (= (:amount (last filtered)) 30.0)))))
;
;(macroexpand-1 '(defmocktest-1 test-fetch-expenses-greater-than
;                               (stubbing-2 [fetch-all-expenses all-expenses]
;                                           (let [filtered (fetch-expenses-greater-than "" "" "" 15.0)]
;                                             (is (= (count filtered) 2))
;                                             (is (= (:amount (first filtered)) 20.0))
;                                             (is (= (:amount (last filtered)) 30.0))))))
;;; expansion:
;'(clojure.test/deftest test-fetch-expenses-greater-than
;   (clojure.core/binding [cia.chapter08.stubbing-expansions/mock-calls (clojure.core/atom {})]
;     (do (clojure.core/binding
;           [fetch-all-expenses (stub-fn fetch-all-expenses all-expenses)]
;           (let [filtered (fetch-expenses-greater-than "" "" "" 15.0)]
;             (is (= (count filtered) 2))
;             (is (= (:amount (first filtered)) 20.0))
;             (is (= (:amount (last filtered)) 30.0)))))))
;
;;; defmocktest macro with mocking and call verification
;'(defn mock-fn [the-function]
;  (stub-fn the-function nil))
;
;'(defmacro mocking [fn-names & body]
;  (let [mocks (map #(list 'mock-fn (keyword %)) fn-names)]
;    `(binding [~@(interleave fn-names mocks)]
;       ~@body)))
;
;'(defmacro verify-call-times-for [fn-name number]
;  `(is (= ~number (count (@mock-calls ~(keyword fn-name))))))
;
;'(defmacro verify-nth-call-args-for [n fn-name & args]
;  `(is (= '~args (nth (@mock-calls ~(keyword fn-name)) (dec ~n)))))
;
;'(defmacro verify-first-call-args-for [fn-name & args]
;  `(verify-nth-call-args-for 1 ~fn-name ~@args))
;
;'(defmocktest-1 test-filter-greater-than
;               (mocking [log-call]
;                        (let [filtered (expenses-greater-than all-expenses 15.0)]
;                          (is (= (count filtered) 2))
;                          (is (= (:amount (first filtered)) 20.0))
;                          (is (= (:amount (last filtered)) 30.0)))
;                        (verify-call-times-for log-call 1)
;                        (verify-first-call-args-for log-call "expenses-greater-than" 15.0)))
;
;;; expansion of mocking macro:
;'(clojure.core/binding [log-call (mock-fn :log-call)])
;;; full expansion
;'(clojure.test/deftest test-filter-greater-than
;  (clojure.core/binding [user/mock-calls (clojure.core/atom {})]
;    (do (mocking [log-call]
;                 (let [filtered (expenses-greater-than all-expenses 15.0)]
;                   (clojure.test/is (= (count filtered) 2))
;                   (clojure.test/is (= (:amount (first filtered)) 20.0))
;                   (clojure.test/is (= (:amount (last filtered)) 30.0)))
;                 (verify-call-times-for log-call 1)
;                 (verify-first-call-args-for log-call "expenses-greater-than" 15.0)))))
;
;;; dummy