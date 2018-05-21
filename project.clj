(defproject restful "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.0-alpha4"]
                 [compojure "1.6.0"]
                 [cheshire "5.8.0"]
                 [korma "0.5.0-RC1"]
                 [ragtime "0.7.2"]
                 [buddy "2.0.0"]
                 [buddy/buddy-auth "2.1.0"]

                 [ring "1.6.3"]
                 [ring-cors "0.1.11"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-mock "0.3.2"]
                 [ring/ring-json "0.5.0-beta1"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring-basic-authentication "1.0.5"]
                 [org.postgresql/postgresql "42.2.1.jre7"]
                 [javax.servlet/servlet-api "3.0-alpha-1"]
                 ]

  :plugins [[lein-ring "0.12.3"]
            [ragtime/ragtime.lein "0.3.9"]]

  :ring {:handler rest-secure.core/app-handler
         :auto-reload? true}

  :main restful.core

  :aot [restful.core]

  :ragtime {:migrations ragtime.sql.files/migrations
            :database   (System/getenv "DATABASE_URL")}

  :profiles {:uberjar {:aot :all}

             :dev {:dependencies [[javax.servlet/servlet-api "3.0-alpha-1"]
                                  [ring-mock "0.1.5"]]}
             }

  )

