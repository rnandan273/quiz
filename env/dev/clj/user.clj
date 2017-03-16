(ns user
  (:require [mount.core :as mount]
            [quiz.figwheel :refer [start-fw stop-fw cljs]]
            quiz.core))

(defn start []
  (mount/start-without #'quiz.core/http-server
                       #'quiz.core/repl-server))

(defn stop []
  (mount/stop-except #'quiz.core/http-server
                     #'quiz.core/repl-server))

(defn restart []
  (stop)
  (start))


