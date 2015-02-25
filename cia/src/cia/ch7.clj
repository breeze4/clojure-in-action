(ns cia.ch7)

(def a-ref (ref 0))

(dosync
 (ref-set a-ref 1))


(defmacro sync-set [r v]
  (list 'dosync
        (list 'ref-set r v)))

(sync-set a-ref 2)

;; unless example of macro expanding language syntax

(defn exhibits-oddity? [x]
  (if (odd? x)
    (println "Very odd!")))

;; unless written as a function
(defn unless [test then]
  (if (not test)
    then))

(defn exhibits-oddity-2? [x]
  (unless (even? x)
          (println "Very odd indeed!")))

(exhibits-oddity-2? 10)
;; arguments are evaluated to a function before the function is called
;; therefore it will always do the println each time
(exhibits-oddity-2? 11)

(defn unless [test then-thunk]
  (if (not test)
    (then-thunk)))

(defn exhibits-oddity-3? [x]
  (unless (even? x)
          #(println (str "Rather odd!" x))))

(exhibits-oddity-3? 10)
(exhibits-oddity-3? 11)
;; not nice because you have to wrap the unless clause
;; in a reader macro #() to pass it correctly so it isn't evaluated first

(defmacro unless-mac [test then]
  (list 'if (list 'not test)
        then))

(defn exhibits-oddity-4? [x]
  (unless-mac (even? x)
              (println "Very odd for reals")))

(exhibits-oddity-4? 10)
(exhibits-oddity-4? 11)

(macroexpand
 '(unless-mac (even? x) (println "Very odd")))

(defmacro unless-mac-2 [test then]
  `(if (not ~test)
     ~then))

(defn exhibits-oddity-5? [x]
  (unless-mac-2 (even? x)
                (println "Way odd dude")))

(exhibits-oddity-5? 10)
(exhibits-oddity-5? 11)

(defmacro infix [expr]
  (let [[left op right] expr]
    (list op left right)))

(macroexpand '(infix (+ 1 2)))
;; doesnt work with 3 arguments boooo
(macroexpand '(infix (+ 1 2 3)))
(macroexpand '(infix (1 + 2)))

(defmacro randomly [& exprs]
  (let [len (count exprs)
        index (rand-int len)
        conditions (map #(list '= index %) (range len))]
    `(cond ~@(interleave conditions exprs))))

(macroexpand '(randomly (println "arg1") (println "arg2") (println "arg3")))

(defn check-credentials [username password]
  true)

(defmacro defwebmethod [name args & exprs]
  `(defn ~name [{:keys ~args}]
     ~@exprs))

(defwebmethod login-user [username password]
  (if (check-credentials username password)
    (str "welcome " username ", " password " still correct")
    (str "login failed")))

(def request {:username "matt" :password "40"})

(login-user request)

(defmacro assert-ture [test-expr]
  (let [[operator lhs rhs] test-expr]
    `(let [lhsv# ~lhs rhsv# ~rhs ret# !~test-expr]
       (if-not ret#
         (throw (RuntimeException. (str '~lhs " is not " '~operator " " rhsv#)))
         true))))

(macroexpand '(assert-true (>= (* 2 4) (/ 18 2))))
(macroexpand-1 '(assert-true (>= (* 2 4) (/ 18 2))))


















