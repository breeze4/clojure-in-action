(ns cia.chapter14.receiver-multiple3
  (:require [cia.chapter14.rabbit-mq :refer :all]))

(defn handle-multiple-messages [handler]
  (doseq [message (message-seq "chapter14-test")]
    (handler message)))

(with-rabbit ["localhost" "guest" "guest"]
             (println "Waiting for messages...")
             (handle-multiple-messages println))

;;