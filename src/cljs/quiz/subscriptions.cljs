(ns quiz.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))




(defn log [s]
  (.log js/console (str s)))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :docs
  (fn [db _]
    (:docs db)))


(reg-sub
  :data-response
  (fn [db _]
    (log (str "SUBS" (:data-response db)))
    (:data-response db)))

