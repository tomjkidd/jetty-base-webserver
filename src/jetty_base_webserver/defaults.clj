(ns jetty-base-webserver.defaults
  (:require [jetty-base-webserver.util :refer [transit-decode]]
            [ring.middleware
             [resource :refer [wrap-resource]]
             [content-type :refer [wrap-content-type]]
             [not-modified :refer [wrap-not-modified]]
             [defaults :refer [wrap-defaults site-defaults]]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]))

(defroutes rest-api
  (route/not-found "<h1>Page not found</h1>"))

(def default-http-handler
  (-> (wrap-defaults rest-api site-defaults)
      (wrap-resource "public")
      (wrap-content-type)
      (wrap-not-modified)))

(def default-ws-handler
  {:on-connect (fn [ws]
                 (prn "Connect:" ws))
   :on-text (fn [ws transit-msg]
              (prn "On-Text:" transit-msg)
              (let [msg (transit-decode transit-msg)]
                (prn  msg)))
   :on-close (fn [ws status-code reason]
               (prn "On-Close:" ws status-code reason))
   :on-error (fn [ws e] (prn "On-Error:" ws e))
   :on-bytes (fn [ws bytes offset len] (prn "On-Bytes:" ws bytes offset len))})
