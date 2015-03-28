(ns cia.chapter14.rabbit-mq
  (:import (com.rabbitmq.client Connection Channel ConnectionFactory QueueingConsumer))
  ;(:require [])
  )

(defn new-connection [host username password]
  (.newConnection
    (doto (ConnectionFactory.)
      (.setVirtualHost "/")
      (.setUsername username)
      (.setPassword password)
      (.setHost host))))

(def ^:dynamic *rabbit-connection*)

(defmacro with-rabbit [[mq-host mq-username mq-password] & exprs]
  `(with-open [connection# (new-connection ~mq-host
                                           ~mq-username ~mq-password)]
     (binding [*rabbit-connection* connection#]
       (do ~@exprs))))

(defn send-message [routing-key message-object]
  (with-open [channel (.createChannel *rabbit-connection*)]
    (.basicPublish channel "" routing-key nil
                   (.getBytes (str message-object)))))

(defn delivery-from [channel consumer]
  (let [delivery (.nextDelivery consumer)]
    (.basicAck channel (.. delivery getEnvelope getDeliveryTag) false)
    (String. (.getBody delivery))))

(defn consumer-for [channel queue-name]
  (let [consumer (QueueingConsumer. channel)
        declare-ok (.queueDeclare channel queue-name false false false {})]
    (.basicConsume channel queue-name consumer)
    consumer))

(defn next-message-from [queue-name]
  (with-open [channel (.createChannel *rabbit-connection*)]
    (let [consumer (consumer-for channel queue-name)]
      (delivery-from channel consumer))))

;(with-rabbit ["localhost" "guest" "guest"]
; (println (next-message-from "chapter14-test")))

;(with-rabbit ["localhost" "guest" "guest"]
; (send-message "chapter14-test" "chapter 14 test method"))
;
;(defn handle-multiple-messages [handler]
;  (loop [message (next-message-from "chapter14-test")]
;    (handler message)
;    (recur (next-message-from "chapter14-test"))))

;(with-rabbit ["localhost" "guest" "guest"]
;             (println "Waiting for messages...")
;             (handle-multiple-messages println))

(defn- lazy-message-seq [channel consumer]
  (lazy-seq
    (let [message (delivery-from channel consumer)]
      (cons message (lazy-message-seq channel consumer)))))

(defn message-seq [queue-name]
  (let [channel (.createChannel *rabbit-connection*)
        consumer (consumer-for channel queue-name)]
    (lazy-message-seq channel consumer)))

;;