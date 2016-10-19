(ns jetty-base-webserver.core
  (require [ring.adapter.jetty9 :as jetty]
           [immuconf.config :as config]
           [com.stuartsierra.component :as component]
           [jetty-base-webserver.defaults :as defaults]))

(def conf
  "Get the values to use to configure the web server"
  (config/load "resources/config.edn"))

(defn- create-server-state
  [{:keys [port ws-timeout-sec join http-handler ws-handler-map on-stop-server] :as opts}]
  (atom {:port port
         :ws-timeout-sec ws-timeout-sec
         :join? join
         :http-handler http-handler
         :ws-handler-map ws-handler-map
         :on-stop-server on-stop-server
         :opts opts}))

(defn- start-server
  "Start a Jetty9 web server"
  [server-state]
  (let [{:keys [http-handler ws-handler-map port join? ws-timeout-sec]} @server-state]
    (jetty/run-jetty http-handler
                     {:port port
                      :join? join?
                      :websockets ws-handler-map
                      :ws-max-idle-time (* ws-timeout-sec 1000)})))

(defrecord Server [state]
  component/Lifecycle
  (start [component]
    (swap! (:state component) (fn [prev] (assoc-in prev [:server] (start-server state))))
    component)
  (stop [component]
    (let [state (:state component)
          s @state
          on-stop-server (:on-stop-server s)
          server (:server s)]
      (jetty/stop-server server)
      (swap! state (fn [prev]
                     (-> (on-stop-server prev)
                         (dissoc :server))))
      component)))

(defn create-server
  "Create a Server component"
  [state-map]
  (map->Server {:state (create-server-state state-map)}))

(defn create-default-server
  "Create the default webserver

  Serves as a starting point for how to configure a more ellaborate server."
  []
  (create-server {:http-handler defaults/default-http-handler
                  :ws-handler-map {"/ws/default" defaults/default-ws-handler}
                  :port 3001
                  :join? true
                  :ws-timeout-sec 300
                  :on-stop-server (fn [state] state)}))

(defn -main
  [& args]
  (let [s (create-default-server)]
    (.start s)
    s))

