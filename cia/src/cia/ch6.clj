(ns cia.ch6)

;; (def adi (atom 0))

;; (defn on-change [the-key the-ref old-value new-value]
;;   (println "Hey, seeing change from" old-value "to" new-value))

;; (add-watch adi :adi-watcher on-change)

;; @adi
;; (swap! adi inc)

;; (remove-watch adi :adi-watcher)
;; (swap! adi inc)

(defn long-calculation [num1 num2]
  (Thread/sleep 5000)
  (* num1 num2))

(defn long-run []
  (let [x (long-calculation 11 13)
        y (long-calculation 13 17)
        z (long-calculation 17 19)]
    (* x y z)))

;; (time (long-run))

(defn fast-run []
  (let [x (future (long-calculation 11 13))
        y (future (long-calculation 13 17))
        z (future (long-calculation 17 19))]
    (* @x @y @z)))

(time (fast-run))

































