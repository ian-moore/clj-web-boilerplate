(ns boilerplate.app-test
  (:require
   [bidi.bidi :as bidi]
   [clojure.test :refer [deftest testing is]] 
   [boilerplate.app :as app]))

(deftest routes
  (testing "undefined routes return 404"
    (let [route-handler (bidi/match-route app/routes "/foobar" :request-method :get)]
      (is (= 404 (:status ((:handler route-handler) nil)))))))
