;; (ns cia.ch2)

;; (def total-expenditure (ref 0))
;; ; reader macro - expands to (deref total-expenditure)


;; (defn add-amount [amount]
;;   ;;not inside a transaction, can't change an object by dereferencing the ref value
;;   (ref-set total-expenditure (+ amount @total-expenditure)))

;; ;(add-amount 5)

;; (defn add-amount-good [amount]
;;   (dosync ;perform inside STM transaction
;;    (ref-set total-expenditure (+ amount @total-expenditure))))

;; ;(add-amount-good 5)

;; (.toUpperCase "clojure")

;; ;(println "hello world")

;; (def users {"kyle" {:password "secretk" :number-pets 2}
;;             "siva" {:password "secrets" :number-pets 4}
;;             "rob" {:password "secretr" :number-pets 6}
;;             "george" {:password "secretg" :number-pets 8}})

;; (defn check-login [username password]
;;   (let [actual-password ((users username) :password)]
;;     (= actual-password password)))

;; ;(doc +)

;; ;(doc check-login)

;; (comment
;;   (defn this-is-not-working [x y]
;;     (+ x y)))

;; (defn addition-fn [x y]
;;   (+ x y))

;; (def addition-function (fn [x y]
;;                          (+ x y)))
;; ;(addition-fn 5 4)
;; ;(addition-function 5 4)

;; (defn average-pets []
;;   (/ (apply + (map :number-pets (vals users))) (count users)))

;; ;(average-pets)

;; (defn average-pets-v2 [users]
;;   (let [user-data (vals users)
;;         number-pets (map :number-pets user-data)
;;         total (apply + number-pets)]
;;     (/ total (count users))))

;; ;(average-pets-v2)

;; (defn average-pets-v3 []
;;   (let [user-data (vals users)
;;         number-pets (map :number-pets user-data)
;;         _ (println "total pets:" number-pets)
;;         total (apply + number-pets)]
;;     (/ total (count users))))

;; ;(average-pets-v3)

;; (do
;;   (let [sum (+ 1 2)
;;         sum (+ 2 sum)]
;;     (+ sum 4)))

;; (defn average-pets-v4 [users]
;;   (try
;;     (let [user-data (vals users)
;;           number-pets (map :number-pets user-data)
;;           total (apply + number-pets)]
;;       (/ total (count users)))
;;     (catch Exception e
;;       (println "Error!")
;;       0)))


;; ;(def no-users {})
;; ;(average-pets-v4 users)

;; (defn prime? [x]
;;   (let [divisors (range 2 (inc (int (Math/sqrt x))))
;;         remainders (map #(rem x %) divisors)]
;;     (not (some zero? remainders))))

;; (defn primes-less-than [n]
;;   (for [x (range 2 (inc n))
;;         :when (prime? x)]
;;     x))

;; ;(primes-less-than 50)

;; (defn pairs-for-primes [n]
;;   (let [z (range 2 (inc n))]
;;     (for [x z y z :when (prime? (+ x y))]
;;       (list x y))))

;; ;(pairs-for-primes 5)

;; (defn final-amount [principle rate time-periods]
;;   (* (Math/pow (+ 1 (/ rate 100)) time-periods) principle))

;; ;; (final-amount 100 20 1)

;; ;; thread-first macro "->"
;; ;; What the thread-first macro does is to take the first argument supplied and place it in
;; ;; the second position of the next expression. It’s called thread-first because it moves
;; ;; code into the position of the first argument of the following form. It then takes the
;; ;; entire  resulting  expression  and  moves  it  into  the  second  position  of  the  following
;; ;; expression,  and  so  on,  until  all  expressions  are  exhausted.
;; (defn final-amount-> [principle rate time-periods]
;;   (-> rate
;;       (/ 100)
;;       (+ 1)
;;       (Math/pow time-periods)
;;       (* principle)))

;; ;; (final-amount 100 20 1)

;; (defn factorial [n]
;;   (apply *
;;          (range 1
;;                 (+ 1
;;                    n))))

;; ;; thread-last ">>" macro
;; ;; This macro expands our factorial->>function to
;; ;; (apply * (range 1 (+ 1 n)))
;; (defn factorial->> [n]
;;   (->> n
;;        (+ 1)
;;        (range 1)
;;        (apply *)))

;; ;; (factorial->> 5)

;; ;; (def the-map {:a 1 :b 2 :c 3})
;; ;; (def my-map {:a 1 :b 2 :c nil})
;; ;; (my-map :a) => 1
;; ;; (:a my-map) => 1

;; (def users {:kyle {
;;                    :date-joined "2009-01-01"
;;                    :summary {
;;                              :average {
;;                                        :monthly 1000
;;                                        :yearly 12000}}}})

;; ;; what you might do to mutate the data in the users map
;; (defn set-average-in [users-map user type amount]
;;   (let [user-map (users-map user)
;;         summary-map (:summary user-map)
;;         averages-map (:average summary-map)]
;;     (assoc users-map user
;;       (assoc user-map :summary
;;         (assoc summary-map :average
;;           (assoc averages-map type amount))))))
;; ;; this is shitty...
;; ;; (set-average-in users :kyle :monthly 2000)
;; (defn average-for [user type]
;;   (type (:average (:summary (user @users)))))

;; ;; alternative! use assoc-in!
;; ;; (assoc-in users [:kyle :summary :average :monthly] 3000)
;; ;; general form: (assoc-in map [key & more-keys] value)

;; ;; getter follows similar pattern
;; ;; (get-in users [:kyle :summary :average :monthly])

;; ;; returns a new map with the increase
;; ;; (update-in users [:kyle :summary :average :monthly] + 500)
;; ;; general form: (update-in map [key & more-keys] update-function & args)
