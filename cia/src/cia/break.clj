;; (ns joy.of.clojure.break)

;; (defn contextual-eval [ctx expr]
;;     (eval
;;         `(let [~@(mapcat (fn [[k v]] [k `'~v]) ctx)]
;;              ~expr)))
;; (defmacro local-context []
;;     (let [symbols (keys &env)]
;;         (zipmap (map (fn [sym] `(quote ~sym)) symbols) symbols)))
;; (defn readr [prompt exit-code]
;;     (let [input (clojure.main/repl-read prompt exit-code)]
;;         (if (= input ::tl)
;;             exit-code
;;              input)))
;; ;;make a break point
;; (defmacro break []
;;   `(clojure.main/repl
;;     :prompt #(print "debug=> ")
;;     :read readr
;;     :eval (partial contextual-eval (local-context))))

;; (defn div [n d] (break) (int (/ n d)))

;; (div 10 0)
;; ;https://coderwall.com/p/xayyvq/break-point-in-clojure
