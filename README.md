# jetty-base-webserver

Abstracts the boilerplate code needed to create an http/ws webserver, just add handlers

## Usage

### Run default server via command line

```bash
lein run
```

### Run default server via repl

```bash
lein repl
```

```clojure
; Create a default server
(def s (create-default-server))
; Start the server
(.start s) ;; try to reach via http or ws

; Stop the server
(.stop s)
```

NOTE: com.stuartsierra/component is used to provide repl driven start/stop.

### Run custom server

In order to do real work, you will have to create relevant http/ws handlers,
see jetty-base-webserver.defaults for a starting point.

The `create-server` function then expects a hashmap with the following key/values

* `:http-handler` A compojure based set of routes to handle http
* `:ws-handler-map` A hashmap of String url paths to ws-handler functions, see defaults
* `:port` Int, The port to use
* `:join?` Boolean, Whether or not the jetty server should join the spawning thread
* `:ws-timeout-sec` Int, How long to keep inactive connections open
* `:on-stop-server` Funtion, A function that allows you to hook into the stop function
    which allows you to manage the state you need for the server.

## Transit support

It is handy to use transit for communication, jetty-base-webserver.util provides
`decode-transit` and `encode-transit` functions so that you can use transit 
with ease.

## License

Copyright © 2016 Tom Kidd

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
