(defproject jetty-base-webserver "0.1.0-SNAPSHOT"
  :description "Abstracts the boilerplate code needed to create an http/ws webserver, just add handlers."
  :url "https://github.com/tomjkidd/jetty-base-webserver"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [info.sunng/ring-jetty9-adapter "0.9.5"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"
                  :exclusions [javax.servlet/servlet-api]]
                 [com.cognitect/transit-clj "0.8.288"]
                 [org.clojure/core.async "0.2.391"]
                 [levand/immuconf "0.1.0"]
                 [com.stuartsierra/component "0.3.1"]]
  :main ^:skip-aot jetty-base-webserver.core
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        ; NOTE: :exclusions are very important here, because the
                        ; dependencies of gniazdo conflict with ring-jetty9-adapter
                        ; Found problem with [lein deps :tree](https://groups.google.com/forum/#!msg/clojure/9cA5hvFJTkw/fnWwxvALd64J)
                        [stylefruits/gniazdo "1.0.1-SNAPSHOT"
                         :exclusions [org.eclipse.jetty.websocket/websocket-client
                                      org.eclipse.jetty/jetty-util
                                      org.eclipse.jetty/jetty-io
                                      org.eclipse.jetty.websocket/websocket-common]]
                        ]
         :source-paths ["dev"]
         :repl-options {
                        ; NOTE: Not using `:init-ns user` because server is focus
                        }}})
