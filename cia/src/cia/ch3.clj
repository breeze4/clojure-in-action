;; (ns cia.ch3)


;; ;; (defn item-total [price quantity]
;; ;;   {:pre [(> price 0) (> quantity 0)]
;; ;;    :post [(> % 0)]} ;; LightTable has a bug and can't evaluate assertions in pre and post!
;; ;;   (* price quantity))

;; ;; (item-total 20 1)

;; ;; (defn basic-item-total [price quantity]
;; ;;   (* price quantity))

;; ;; (defn with-line-item-conditions [f price quantity]
;; ;;   {:pre [(> price 0) (> quantity 0)]
;; ;;    :post [(> % 1)]}
;; ;;   (apply f price quantity))

;; ;; (def users [
;; ;;             {:username "kyle"
;; ;;              :balance 175.00
;; ;;              :member-since "2009-04-16"}
;; ;;             {:username "zak"
;; ;;              :balance 12.95
;; ;;              :member-since "2009-02-01"}
;; ;;             {:username "rob"
;; ;;              :balance 98.50
;; ;;              :member-since "2009-03-30"}
;; ;;             ])

;; ;; (defn username [user]
;; ;;   (user :username))

;; ;; (defn balance [user]
;; ;;   (user :balance))

;; ;; (defn sorter-using [ordering-fn]
;; ;;   (fn [users]
;; ;;     (sort-by ordering-fn users)))

;; ;; (def poorest-first (sorter-using balance))

;; ;; (def alphabetically (sorter-using username))

;; ;; (map (fn [user] (user :member-since)) users)

;; ;; (map #(% :member-since) users)

;; ;; (map :member-since users)

;; ;; (:login (users 1) :not-found)
;; ;; (:login (users 1))

;; ;; (def ^:dynamic *db-host* "localhost")

;; ;; (defn expense-report [start-date end-date]
;; ;;   (println *db-host*)) ;; can do real work

;; ;; (binding [*db-host* "production"]
;; ;;   (expense-report "2010-01-01" "2010-01-07"))

;; ;; (def ^:dynamic *eval-me* 10)
;; ;; (defn print-the-var [label]
;; ;;   (println label *eval-me*))
;; ;; (print-the-var "A:")
;; ;; (binding [*eval-me* 20] ;; the first binding
;; ;;   (print-the-var "B:")
;; ;;   (binding [*eval-me* 30] ;; the second binding
;; ;;     (print-the-var "C:"))
;; ;;   (print-the-var "D:"))
;; ;; (print-the-var "E:")

;; ;; (def dog {:sound #(str "wooooof")})
;; ;; (def cat {:sound #(str "mewwww")})

;; ;; (defn ^:dynamic speak [] (println "eh?"))

;; ;; (defmacro with-animal [animal & body]
;; ;;   `(binding [speak (:sound ~animal)]
;; ;;      ~@body))

;; ;; (with-animal dog
;; ;;   (println (str "Dog: " (speak) "\n")))

;; ;; (with-animal cat
;; ;;   (println (str "Cat: " (speak) "\n")))

;; ;; (defmacro with-animal [animal & body]
;; ;;   `(let [~'speak (:sound ~animal)]
;; ;;      ~@body))

;; ;; (with-animal dog
;; ;;   (println (str "Dog: " (speak) "\n")))

;; ;; (defn ^:dynamic twice [x]
;; ;;   (println "original function")
;; ;;   (* 2 x))

;; ;; (defn call-twice [y]
;; ;;   (twice y))
;; ;; (defn with-log [function-to-call log-statement]
;; ;;   (fn [& args]
;; ;;     (println log-statement)
;; ;;     (apply function-to-call args)))
;; ;; (call-twice 10)
;; ;; (binding [ twice (with-log twice "Calling the twice function")]
;; ;;   (call-twice 20))
;; ;; (call-twice 30)

;; ;; lazy evaluation at work:
;; ;; (def ^:dynamic *factor* 10)

;; ;; (defn multiply [x]
;; ;;   (* x *factor*))

;; ;; (map multiply [1 2 3 4 5])

;; ;; A call to map returns a lazy sequence and this sequence isn’t realized until it’s needed.
;; ;; Whenever that happens (in this case. as the REPL tries to print
;; ;; it), the execution no longer occurs inside the binding form, and so *factor* reverts
;; ;; to its root binding of 10. This is why you get the same answer as in the previous case.
;; ;; (binding [*factor* 20]
;; ;;   (map multiply [1 2 3 4 5]))

;; ;; (map multiply [1 2 3 4 5])

;; ;; force evaluation of lazy sequences with doall macro
;; ;; (binding [*factor* 20]
;; ;;   (doall (map multiply [1 2 3 4 5])))

;; ;; (let [x 10
;; ;;       y 20]
;; ;;   (println "x, y:" x "," y))

;; ;; (defn upcased-names [names]
;; ;;   (let [up-case (fn [name]
;; ;;                   (.toUpperCase name))]
;; ;;     (map up-case names)))
;; ;; (upcased-names ["bob" "geORge" "sam"])

;; ;; (defn upcased-names [names]
;; ;;   (map (fn [name]
;; ;;          (.toUpperCase name)) names))
;; ;; (upcased-names ["bob" "geORge" "sam"])

;; ;; (def ^:dynamic *factor* 10)
;; ;; (binding [*factor* 20]
;; ;;   (println *factor*)
;; ;;   (doall (map multiply [1 2 3 4 5])))

;; ;; (let [*factor* 20]
;; ;;   (println *factor*)
;; ;;   (doall (map multiply [1 2 3 4 5])))

;; ;; (defn create-scaler [scale]
;; ;;   (fn [x]
;; ;;     (* x scale)))

;; ;; (def percent-scaler (create-scaler 100))

;; ;; (percent-scaler 0.59)

;; ;; defn- creates private functions (can only be called inside same namespace)
;; ;; defn creates public functions

;; ;; (ns org.currylogic.damages.http.expenses
;; ;;   (:require [clojure.data.json :as json])
;; ;;   (:require [clojure.xml :as xml]))

;; ;; (defn import-transactions-xml-from-bank [url]
;; ;;   (let [xml-document (parse url)]
;; ;;     ;; more code here
;; ;;     ))

;; ;; (defn totals-by-day [start-date end-date]
;; ;;   (let [expenses-by-day (load-totals start-date end-date)]
;; ;;     (encode-to-str expenses-by-day)))

;; ;; (defn print-amounts [[amount-1 amount-2]]
;; ;;   (println "amounts are:" amount-1 "and" amount-2))

;; ;; (print-amounts [10.95 31.45])

;; ;; (defn print-amounts-multiple [[amount-1 amount-2 & remaining]]
;; ;;   (println "Amounts are:" amount-1 "," amount-2 "and" remaining))

;; ;; (print-amounts-multiple [10.95 31.45 22.36 2.95])

;; ;; (defn print-all-amounts [[amount-1 amount-2 & remaining :as all]]
;; ;;   (println "Amounts are:" amount-1 "," amount-2 "and" remaining)
;; ;;   (println "Also, all the amounts are:" all))

;; ;; (print-all-amounts [10.95 31.45 22.36 2.95])

;; ;; (defn print-first-category [[[category amount] & _ ]]
;; ;;   (println "First category was:" category)
;; ;;   (println "First amount was:" amount)
;; ;;   (println "Rest of stuff was:" _))

;; ;; (def expenses [[:books 49.95] [:coffee 4.95] [:caltrain 2.25]])
;; ;; (print-first-category expenses)

;; ;; (defn describe-salary-3 [{first :first-name
;; ;;                           last :last-name
;; ;;                           annual :salary
;; ;;                           bonus :bonus-percentage
;; ;;                           :or {bonus 5}}]
;; ;;   (println first last "earns" annual "with a" bonus "percent bonus"))

;; ;; (def a-user {:first-name "pascal"
;; ;;              :last-name "dylan"
;; ;;              :salary 85000
;; ;;              :bonus-percentage 20})
;; ;; (describe-salary-3 a-user)

;; ;; (def another-user {:first-name "basic"
;; ;;                    :last-name "groovy"
;; ;;                    :salary 70000})
;; ;; (describe-salary-3 another-user)

;; ;; (defn describe-person [{first :first-name
;; ;;                         last :last-name
;; ;;                         bonus :bonus-percentage
;; ;;                         :or {bonus 5}
;; ;;                         :as p}]
;; ;;   (println "Info about" first last "is:" p)
;; ;;   (println "Bonus is:" bonus "percent"))

;; ;; (def third-user {:first-name "lambda"
;; ;;                  :last-name "curry"
;; ;;                  :salary 95000})
;; ;; (describe-person third-user)

;; ;; (defn greet-user [{:keys [first-name last-name]}]
;; ;;   (println "Welcome," first-name last-name))

;; ;; (def roger {:first-name "roger"
;; ;;             :last-name "mann"
;; ;;             :salary 65000})
;; ;; (greet-user roger)

;; ;; (defn describe-salary [person]
;; ;;   (let [first (:first-name person)
;; ;;         last (:last-name person)
;; ;;         annual (:salary person)]
;; ;;     (println first last "earns" annual)))

;; ;; (describe-salary roger)

;; ;; destructed form equivalent to let form
;; ;; (defn describe-salary-2 [{first :first-name
;; ;;                           last :last-name
;; ;;                           annual :salary}]
;; ;;   (println first last "earns" annual))

;; ;; (describe-salary-2 roger)

;; ;; metadata
;; (def untrusted (with-meta {:command "clean-table" :subject
;;                            (with-meta {:name "users"} {:safe false :io true}) }
;;                  {:safe false :io true :arbitrary-string "fram"}))

;; untrusted
;; (meta untrusted)
;; (meta (:subject untrusted))

;; ;; metadata carries over to new objects from existing objects
;; (def still-suspect (assoc untrusted :complete? false))

;; (meta still-suspect)

;; ;; can add metadata to functions and macros too
;; (defn
;;   testing-meta "testing metadata for functions"
;;   {:safe true :console true}
;;   []
;;   (println "Hello from meta!"))

;; (meta testing-meta)
;; ;; This returns nilbecause the metadata is associated with the var testing-metanot the
;; ;; function object itself. To access the metadata, you’d have to pass the testing-metavar
;; ;; to the metafunction.
;; (meta (var testing-meta))

;; ;; Clojure  internally  uses  metadata  quite  a  lot;  for
;; ;; example,  the :doc key  is  used  to  hold  the  documentation  string  for  functions  and
;; ;; macros, the :macrokey is set to truefor functions that are macros, the :filekey is
;; ;; used to keep track of what source file something was defined in, and so on.






















