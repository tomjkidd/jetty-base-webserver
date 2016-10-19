(ns user
  (:require [gniazdo.core :as ws]
            [cognitect.transit :as transit]
            [immuconf.config :as config]
            [jetty-base-webserver.util :refer [transit-encode]]))

(def conf
  "Load the configuration file, an edn hashmap"
  (config/load "resources/config.edn"))

(def ws-url
  "Use conf to determine the websocket url to use"
  (str "ws://"
       (config/get conf :ws-host)
       (when-let [port (config/get conf :ws-port)]
         (str ":" port))
       (config/get conf :ws-path)))

(defn socket*
  "Create a websocket connection given the url"
  [url]
  (println url)
  (ws/connect
   url
   :on-receive #(prn 'received %)))

(defn send-msg*
  "Send the given socket a transit message"
  [socket msg]
  (ws/send-msg socket (transit-encode msg)))

(defn close*
  "Close the websocket connection given a socket"
  [socket]
  (ws/close socket))

(def socket
  "Hold an active websocket connection"
  (atom nil))

(defn connect
  "Connect to the default websocket"
  []
  (reset! socket (socket* ws-url)))

(defn send-msg
  "Send a message to the default websocket connection"
  [msg]
  (send-msg* @socket msg))

(defn close
  "Close the default websocket connection"
  []
  (close* @socket))
