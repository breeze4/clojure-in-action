;; (ns cia.ch7)

;; (def a-ref (ref 0))

;; (dosync
;;  (ref-set a-ref 1))


;; (defmacro sync-set [r v]
;;   (list 'dosync
;;         (list 'ref-set r v)))

;; (sync-set a-ref 2)

;; ;; unless example of macro expanding language syntax

;; (defn exhibits-oddity? [x]
;;   (if (odd? x)
;;     (println "Very odd!")))

;; ;; unless written as a function
;; (defn unless [test then]
;;   (if (not test)
;;     then))

;; (defn exhibits-oddity-2? [x]
;;   (unless (even? x)
;;           (println "Very odd indeed!")))

;; (exhibits-oddity-2? 10)
;; ;; arguments are evaluated to a function before the function is called
;; ;; therefore it will always do the println each time
;; (exhibits-oddity-2? 11)

;; (defn unless [test then-thunk]
;;   (if (not test)
;;     (then-thunk)))

;; (defn exhibits-oddity-3? [x]
;;   (unless (even? x)
;;           #(println (str "Rather odd!" x))))

;; (exhibits-oddity-3? 10)
;; (exhibits-oddity-3? 11)
;; ;; not nice because you have to wrap the unless clause
;; ;; in a reader macro #() to pass it correctly so it isn't evaluated first

;; (defmacro unless-mac [test then]
;;   (list 'if (list 'not test)
;;         then))

;; (defn exhibits-oddity-4? [x]
;;   (unless-mac (even? x)
;;               (println "Very odd for reals")))

;; (exhibits-oddity-4? 10)
;; (exhibits-oddity-4? 11)

;; (macroexpand
;;  '(unless-mac (even? x) (println "Very odd")))

;; (defmacro unless-mac-2 [test then]
;;   `(if (not ~test)
;;      ~then))

;; (defn exhibits-oddity-5? [x]
;;   (unless-mac-2 (even? x)
;;                 (println "Way odd dude")))

;; (exhibits-oddity-5? 10)
;; (exhibits-oddity-5? 11)

;; (defmacro infix [expr]
;;   (let [[left op right] expr]
;;     (list op left right)))

;; (macroexpand '(infix (+ 1 2)))
;; ;; doesnt work with 3 arguments boooo
;; (macroexpand '(infix (+ 1 2 3)))
;; (macroexpand '(infix (1 + 2)))

;; (defmacro randomly [& exprs]
;;   (let [len (count exprs)
;;         index (rand-int len)
;;         conditions (map #(list '= index %) (range len))]
;;     `(cond ~@(interleave conditions exprs))))

;; (macroexpand '(randomly (println "arg1") (println "arg2") (println "arg3")))

;; (defn check-credentials [username password]
;;   true)

;; (defmacro defwebmethod [name args & exprs] ;; note: not actually a name macro, it's a parameter!
;;   `(defn ~name [{:keys ~args}] ;; syntax-quote to resolve the symbols in their current context
;;      ;; extracts keys from the args
;;      ~@exprs))

;; (defwebmethod login-user [username password]
;;   (if (check-credentials username password)
;;     (str "welcome " username ", " password " still correct")
;;     (str "login failed")))

;; (def request {:username "matt" :password "40"})

;; (login-user request)

;; (macroexpand-1 '(defwebmethod login-user [username password]
;;                   (if (check-credentials username password)
;;                     (str "welcome " username ", " password " still correct")
;;                     (str "login failed"))))

;; ;; result of macroexpand-1
;; (clojure.core/defn login-user [{:keys [username password]}]
;;   (if (check-credentials username password)
;;     (str "welcome " username ", " password " still correct")
;;     (str "login failed")))

;; (defmacro assert-true [test-expr]
;;   (let [[operator lhs rhs] test-expr] ;; binding form to separate test-expr into parts
;;     `(let [lhsv# ~lhs
;;            rhsv# ~rhs
;;            ret# ~test-expr]
;;        ;; syntax-quote resolves the symbols in the current context - if the symbol is non-namespace-qualified
;;        ;; AND it ends in #, see lhsv and rhsv - then x# resolves to something that looks like x_123_auto
;;        (if-not ret# ;; simple evaluation of the bound ret# auto symbol
;;          (throw (RuntimeException. (str '~lhs " is not " '~operator " " rhsv#)))
;;          true))))

;; (macroexpand '(assert-true (>= (* 2 4) (/ 18 2))))

;; (let* [lhsv__8333__auto__ (* 2 4) rhsv__8334__auto__ (/ 18 2) ret__8335__auto__ (>= (* 2 4) (/ 18 2))]
;;       (clojure.core/if-not ret__8335__auto__
;;                            (throw (java.lang.RuntimeException.
;;                                    (clojure.core/str
;;                                     (quote (* 2 4)) " is not "
;;                                     (quote >=) " " rhsv__8334__auto__))) true))


;; (macroexpand-1 '(assert-true (>= (* 2 4) (/ 18 2))))

;; (clojure.core/let
;;  [lhsv__6321__auto__ (* 2 4)
;;   rhsv__6322__auto__ (/ 18 2)
;;   ret__6323__auto__ (>= (* 2 4) (/ 18 2))]
;;  (clojure.core/if-not ret__6323__auto__
;;                       (throw (java.lang.RuntimeException.
;;                               (clojure.core/str
;;                                (quote (* 2 4)) " is not "
;;                                (quote >=) " " rhsv__6322__auto__))) true))


;; ;; written with an anonymous function, easier to reason about in some cases
;; (let [a 1
;;       f (future (fn [] (+ a 2)))] ; Or alternatively, #(+ a 2)
;;   (deref f)) ; returns 3
;; ;; same thing, written as a macro that doesn't take an anonymous function
;; (let [a 1
;;       f (future (+ a 2))]
;;   (deref f)) ; returns 3

;; (macroexpand-1 `(1 2 ~(list 3 4)))
;; (macroexpand-1 `(1 2 ~@(list 3 4)))
;; (macroexpand '(+ 1 ~@(reverse `(+ 1 1))))
;; (reverse '(+ 1 1))
;; (map reverse '((+ 1 1) (+ 2 2) (+ 3 3)))

;; (def x 5)
;; '(+ 1 x)
;; (list '+ '1 x)
;; `(+ 1 ~x) ;; can't resolve in lighttable apparently

;; (defmacro logging [fnname args & body]
;;   `(defn ~fnname ~args
;;      (println (str "hello there: " ~@args))
;;      ~@body))

;; (defrename greeter [username]
;;   (str (str "hello there: " username)))

;; (macroexpand-1 '(logging greeter [username]
;;                          (str (str "hello there: " username))))

;; (greeter "myname")
;; (greeter "anothername")
;; (name `greeter)


;; ;; improved assert-true macro
;; (defmacro assert-true [test-expr]
;;   (if-not (= 3 (count test-expr))
;;     (throw (RuntimeException.
;;             "Argument must be of the form
;;             (operator test-expr expected-expr)")))
;;   (if-not (some #{(first test-expr)} '(< > <= >= = not=))
;;     ;; some behaves as a collection.contains() call would, neat
;;     (throw (RuntimeException.
;;             "operator must be one of < > <= >= = not=")))
;;   (let [[operator lhs rhs] test-expr]
;;     `(let [lhsv# ~lhs
;;            rhsv# ~rhs
;;            ret# ~test-expr]
;;        (if-not ret#
;;          (throw (RuntimeException.
;;                  (str '~lhs " is not " '~operator " " rhsv#)))
;;          true))))

;; (assert-true (>= (* 2 4) (/ 18 2) (+ 2 5)))
;; (assert-true (<> (* 2 4) (/ 16 2)))














