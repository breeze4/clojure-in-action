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
























