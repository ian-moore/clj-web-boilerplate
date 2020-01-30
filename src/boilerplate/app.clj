(ns boilerplate.app
  (:require
   [bidi.ring]
   [clojure.java.io :as io]
   [integrant.core :as integrant]
   [muuntaja.middleware]
   [ring.adapter.jetty :as ring-jetty]
   [ring.util.http-response :as http-response]
   [taoensso.timbre :as timbre])
  (:import
   (org.eclipse.jetty.server Server)))

(defn- set-uncaught-exception-handler!
  []
  (Thread/setDefaultUncaughtExceptionHandler
   (reify Thread$UncaughtExceptionHandler
     (uncaughtException [_this thread ex]
       (timbre/fatal ex "Uncaught exception on" (.getName thread))))))

(def routes
  ["/"
   {"info" {:get (fn [_] (http-response/no-content))}
    true (fn [_] (http-response/not-found))}])

(def app
  (-> routes
      bidi.ring/make-handler
      muuntaja.middleware/wrap-format))

(defmethod integrant/init-key :boilerplate.integrant/jetty
  [_ opts]
  (ring-jetty/run-jetty app opts))

(defmethod integrant/halt-key! :boilerplate.integrant/jetty
  [_ ^Server server]
  (.stop server))

(defn -main
  [& _args]
  (set-uncaught-exception-handler!)  
  (timbre/info "Starting app")
  (let [system (integrant/read-string (slurp (io/resource "system.edn")))]
    (integrant/load-namespaces system)
    (integrant/init system))
  (timbre/info "App started"))
