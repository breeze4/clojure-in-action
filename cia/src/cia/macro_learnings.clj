(ns cia.macro-learnings)


;; After (macroexpansion)
;;   (let [init 1]
;;     (let [res0 (+ init 2)]
;;       (let [res1 (+ 3 res0)]
;;         (let [res2 (- 50 res1)]
;;           (let [res3 (/ res2 2)]
;;             res3)))))

(defn replace-if-underscore [element val]
  (if (= element '_)
    val
    element))

(replace-if-underscore '_ 1)
(replace-if-underscore '+ 1)

(defn replace-underscores [form val]
  (map #(replace-if-underscore % val) form))

(replace-underscores '(+ 2 _) 1)
(replace-underscores '(+ 2 3) 1)

(defn convert-forms [val [next-form & other-forms]]               ; 1
  (if (nil? next-form)                                            ; 2
    val
    (let [next-val (gensym)]                                      ; 3
      `(let [~next-val ~(replace-underscores next-form val)]      ; 4
         ~(convert-forms next-val other-forms)))))                ; 5

(convert-forms 2 '((+ _ 1) (+ 4 _)))
; =>
; (clojure.core/let [G__1166 (+ 2 1)]
;   (clojure.core/let [G__1167 (+ 4 G__1166)]
;     G__1167))

(defmacro ->>> [init & forms]
  (convert-forms init forms))

(->>> 1       ; initial value of 1
    (+ _ 2)     ; with previous result of 1, semantically looks like: (+ 1 2)
    (+ 3 _)     ; previous result of 3, (+ 3 3)
    (- 50 _)    ; previous result of 6, (- 50 6)
    (/ _ 2))    ; previous result of 44, (/ 40 2) => final result: 22

(macroexpand-1 '(->>> 1       ; initial value of 1
                      (+ _ 2)     ; with previous result of 1, semantically looks like: (+ 1 2)
                      (+ 3 _)     ; previous result of 3, (+ 3 3)
                      (- 50 _)    ; previous result of 6, (- 50 6)
                      (/ _ 2)))

