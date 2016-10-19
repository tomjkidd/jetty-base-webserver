(ns jetty-base-webserver.util
  (:require [cognitect.transit :as transit]))

(import [java.io ByteArrayInputStream ByteArrayOutputStream]
        [java.nio.charset StandardCharsets])

(defn transit-decode
  "A helper function that allows you to decode a transit :json message"
  [transit-msg]
  (let [source (-> (.getBytes transit-msg StandardCharsets/UTF_8)
                   (ByteArrayInputStream.))
        r (transit/reader source :json)
        msg (transit/read r)]
    msg))

(defn transit-encode
  "A helper function that allows you to encode a message as transit data"
  [msg]
  (let [out (ByteArrayOutputStream. 4096)
        tw (transit/writer out :json)
        transit-msg (do
                      (transit/write tw msg)
                      (.toString out))]
    transit-msg))
