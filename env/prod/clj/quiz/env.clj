(ns quiz.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[quiz started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[quiz has shut down successfully]=-"))
   :middleware identity})
