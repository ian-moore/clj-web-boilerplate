(ns user
  (:require
   [clojure.java.io :as io]
   [clojure.repl :refer :all]
   [clojure.tools.namespace.repl :refer [refresh]]
   [integrant.core :as integrant]))

(def system (volatile! nil))

(defn init
  []
  (vreset! system (integrant/read-string (slurp (io/resource "system.edn")))))

(defn start
  []
  (vswap! system integrant/init))

(defn stop
  []
  (vswap! system (fn [system] (when system (integrant/halt! system)))))

(defn- go*
  []
  (init)
  (start))

(defn go
  []
  (stop)
  (refresh :after 'user/go*))
