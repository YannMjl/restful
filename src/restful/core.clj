(ns restful.core
  (:use [clojure.java.io]
        [clojure.set]
        [compojure.core :refer :all]
        [ring.middleware.json])

  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            [ring.middleware.basic-authentication :refer [wrap-basic-authentication]]

            [buddy.hashers :as hashers]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            )
  )

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn str-to [number]
  (apply str (interpose ", " (range 1 (inc number))))
  )

(defn str-from [number]
  (apply str (interpose ", " (reverse (range 1 (inc number)))))
  )
;----------------------------------------------------------------------------------------------------------------------*

(defn unauthorized-handler [req msg]
  {:status 401
   :body {:status :error
          :message (or msg "User not authorized")}})

(defn app-handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain;=us-ascii"
             "Access-Control-Allow-Credentials" "false"}
   :body (str request)})

(defroutes app-routes
           ;(GET "/info" [] {the-user :basic-authentication } {:status 200 :header {} :body (str "Your email is " (:email the-user) " your password is " (:password the-user))})
          
           (GET "/" [] (apply str "<h2>Hello, World!</h2>"))
           (GET "/count-up/:to" [to] (str-to (Integer. to)))
           (GET "/count-down/:from" [from] (str-from (Integer. from)))
           (route/not-found "<h1>Page not found or does not exit</h1>")
           )

;----------------------------------------------------------------------------------------------------------------------*
;                                                                                                                      *
;----------------------------------------------------------------------------------------------------------------------*
; Behold, our middleware! Note that it's common to prefix our middleware name
; with "wrap-", since it surrounds any routes an other middleware "inside"
(defn wra-log-request [handler]
  (fn [req]             ; return handler function
    (println req)       ; perform logging
    (handler req))      ; pass the request through to the inner handler
  )

(defn authenticated? [username password]
  (and (= username "admin")
       (= password "pass")
       {:user username :password password})
  )

; We can attach our middleware directly to the main application handler. All
; requests/responses will be "filtered" through our logging handler.
(def app
  (-> app-routes
      wra-log-request
      wrap-json-response
      wrap-json-body
      (wrap-basic-authentication authenticated?)
      )
  ; With this middleware in place, we are all set to parse JSON request bodies and
  ; serve up JSON responses
  )

;----------------------------------------------------------------------------------------------------------------------*
; authentication with buddy.auth                                                                                       *
;----------------------------------------------------------------------------------------------------------------------*

(defn -main []
  (jetty/run-jetty app {:port 5000})
  )
;----------------------------------------------------------------------------------------------------------------------*
