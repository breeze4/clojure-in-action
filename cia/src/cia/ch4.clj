(ns cia.ch4)

;; (defn fee-amount [percentage user]
;;   (float (* 0.01 percentage (:salary user))))

;; ;; without multimethods, this cond will get gnarly
;; (defn affiliate-fee-cond [user]
;;   (cond
;;    (= :google.com (:referrer user)) (fee-amount 0.01 user)
;;    (= :mint.com (:referrer user)) (fee-amount 0.03 user)
;;    :default (fee-amount 0.02 user)))

;; ;; (defmulti name dispatch-fn & options)
;; ;; (defmethod multifn dispatch-value & fn-tail)

;; ;; (defmulti affiliate-fee :referrer)
;; (defmulti affiliate-fee :referrer :default :else)

;; (defmethod affiliate-fee :default [user]
;;   (fee-amount 0.02 user))

;; (defmethod affiliate-fee :else [user]
;;   (fee-amount 0.02 user))

;; (defmethod affiliate-fee :mint.com [user]
;;   (fee-amount 0.03 user))

;; (defmethod affiliate-fee :google.com [user]
;;   (fee-amount 0.01 user))

;; (def user-1 {:login "rob" :referrer :mint.com :salary 100000})
;; (def user-2 {:login "kyle" :referrer :google.com :salary 90000})
;; (def user-3 {:login "celeste" :referrer :yahoo.com :salary 70000})

;; (affiliate-fee user-1)
;; (affiliate-fee user-2)
;; (affiliate-fee user-3)

;; ;; it returns one of the four possible ratings out of ::bronze,
;; ;; ::silver, ::gold, or ::platinum. The extra colon resolves each of these keywords to
;; ;; the namespace it’s used in.
;; ;; this could be doing anything though, database or service calls, whatever
;; (defn profit-rating [user]
;;   (let [ratings [::bronze ::silver ::gold ::platinum]]
;;     (nth ratings (rand-int (count ratings)))))

;; ;; This  returns  a  vector  containing  two  values,  for  instance, [:mint.com ::bronze],
;; ;; [:google.com ::gold], and [:mint.com ::platinum].
;; (defn fee-category [user]
;;   [(:referrer user) (profit-rating user)])

;; (defmulti profit-based-affiliate-fee fee-category)
;; (defmethod profit-based-affiliate-fee [:mint.com ::bronze] [user]
;;   (fee-amount 0.03 user))
;; (defmethod profit-based-affiliate-fee [:mint.com ::silver] [user]
;;   (fee-amount 0.04 user))
;; (defmethod profit-based-affiliate-fee [:mint.com ::gold] [user]
;;   (fee-amount 0.05 user))
;; (defmethod profit-based-affiliate-fee [:mint.com ::platinum] [user]
;;   (fee-amount 0.05 user))
;; (defmethod profit-based-affiliate-fee [:google.com ::gold] [user]
;;   (fee-amount 0.03 user))
;; (defmethod profit-based-affiliate-fee [:google.com ::platinum] [user]
;;   (fee-amount 0.03 user))
;; (defmethod profit-based-affiliate-fee :default [user]
;;   (fee-amount 0.02 user))

;; (derive ::bronze ::basic)
;; (derive ::silver ::basic)
;; (derive ::gold ::premier)
;; (derive ::platinum ::premier)

;; (isa? ::platinum ::premier)
;; (isa? ::bronze ::basic)

;; ;; rewrite to take advantage of the derive function
;; (defmulti affiliate-fee-for-hierarchy fee-category)
;; (defmethod affiliate-fee-for-hierarchy [:mint.com ::bronze] [user]
;;   (fee-amount 0.03 user))
;; (defmethod affiliate-fee-for-hierarchy [:mint.com ::silver] [user]
;;   (fee-amount 0.04 user))
;; (defmethod affiliate-fee-for-hierarchy [:mint.com ::premier] [user]
;;   (fee-amount 0.05 user))
;; (defmethod affiliate-fee-for-hierarchy [:google.com ::premier] [user]
;;   (fee-amount 0.03 user))
;; (defmethod affiliate-fee-for-hierarchy :default [user]
;;   (fee-amount 0.02 user))


;; (def aNode {:type :assignment :expr "assignment"})

;; (def vNode {:type :variable-ref :expr "variableref"})

;; (defmulti checkValidity :type)

;; (defmethod checkValidity :assignment [node]
;;   (println "checking :assignment, expression is" (:expr node)))

;; (defmethod checkValidity :variable-ref [node]
;;   (println "checking :variable-ref, expression is" (:expr node)))

;; (defmulti generateASM :type)

;; (defmethod generateASM :assignment [node]
;;   (println "gen ASM for :assignment, expr is" (:expr node)))

;; (defmethod generateASM :variable-ref [node]
;;   (println "gen ASM for :variable-ref, expr is" (:expr node)))
