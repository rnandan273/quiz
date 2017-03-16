(ns quiz.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [quiz.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[quiz started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[quiz has shut down successfully]=-"))
   :middleware wrap-dev})
