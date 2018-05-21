(ns restful.core-test
  (:require [clojure.test :refer :all]
            [restful.core :refer :all]
            [ring.mock.request :as mock]
            )
  )

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

;----------------------------------------------------------------------------------------------------------------------*
;this section content queries functions that read the report data stored in the postgres                               *
;database which returns a sub report by organization name or the data uploaded                                         *
;----------------------------------------------------------------------------------------------------------------------*

(deftest test-app
  (testing "users endpoint"
    (let [response (app (mock/request :get "/users"))]
      (is (= (:status response) 200))
      (is (= (get-in response [:headers "Content-Type"]) "application-json"))))

  (testing "lists endpoint"
    (let [response (app (mock/request :get "/lists"))]
      (is (= (:status response) 200))
      (is (= (get-in response [:headers "Content-Type"]) "application-json"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/bogus-route"))]
      (is (= (:status response) 404)))))