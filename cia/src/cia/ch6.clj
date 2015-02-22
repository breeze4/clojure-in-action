(ns cia.ch6)

;; altering refs - 3 options:
;; ref-set
;; alter
;; commute
(def all-users (ref {}))

(deref all-users)
@all-users

(dosync
 (ref-set all-users {}))

(defn new-user [id login monthly-budget]
  {:id id
   :login login
   :monthly-budget monthly-budget
   :total-expenses 0})

(defn add-new-user [login budget-amount]
  (dosync
   (let [current-number (count @all-users)
         user (new-user (inc current-number) login budget-amount)]
     (alter all-users assoc login user))))

;; When the alter function is applied, it checks to see if the value of the ref has changed because of another committed transaction.
;; This causes the current transaction to fail and for it to be retried.
;; The commute function doesn’t behave this way; instead, execution proceeds forward
;; and all calls to commute are handled at the end of the transaction.
;; (commute ref function & args)

;; agents

(def total-cpu-time (agent 0))

(deref total-cpu-time)

@total-cpu-time

(send total-cpu-time + 700)

@total-cpu-time

(dotimes [n 100]
  (send total-cpu-time + n)
  ;;   (println @total-cpu-time)
  (await total-cpu-time))

(def bad-agent (agent 10))

(send bad-agent / 0)

@bad-agent

(agent-errors bad-agent)

(require '[clojure.string :as str])
(let [e (first (agent-errors bad-agent))
      st (.getStackTrace e)]
  (println (.getMessage e))
  (println (str/join "\n" st)))

(clear-agent-errors bad-agent)

(let [e (first (agent-errors bad-agent))
      st (.getStackTrace e)]
  (println (.getMessage e))
  (println (str/join "\n" st)))

(defn run [nvecs nitems nthreads niters]
  (let [vec-refs (vec (map (comp ref vec)
                           (partition nitems (range (* nvecs nitems)))))
        swap #(let [v1 (rand-int nvecs)
                    v2 (rand-int nvecs)
                    i1 (rand-int nitems)
                    i2 (rand-int nitems)]
                (dosync
                 (let [temp (nth @(vec-refs v1) i1)]
                   (alter (vec-refs v1) assoc i1 (nth @(vec-refs v2) i2))
                   (alter (vec-refs v2) assoc i2 temp))))
        report #(do
                  (prn (map deref vec-refs))
                  (println "Distinct:"
                           (count (distinct (apply concat (map deref vec-refs))))))]
    (report)
    (dorun (apply pcalls (repeat nthreads #(dotimes [_ niters] (swap)))))
    (report)))

(run 100 10 10 100000)
























