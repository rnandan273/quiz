(ns quiz.handlers
  (:require [quiz.db :as db]
            [re-frame.core :as rf]
            [re-frame.core :refer [dispatch reg-event-db]]
            [cljs.core.async :as async :refer [chan close!]]
            [clojure.walk :as walk]
            [cljs.reader :as reader]
            [ajax.core :refer [GET POST]]
            [kvlt.core :as kvlt])
  (:require-macros
   [cljs.core.async.macros :refer [go alt!]]))



(defn log [s]
  (.log js/console (str s)))

(defn error-handler [ch response]
  (log response)
  (log "DONE"))

(defn response-handler [ch response]
  (go (>! ch response)(close! ch))
  (log "DONE"))

(defn do-http-post [url doc]
  (log "POSTING ---->")
  (log (str "POST " url (clj->js doc)))
  (let [ch (chan 1)]
    (POST url {:params  (clj->js doc) :format :json :handler (fn [response] (response-handler ch response))
               :error-handler (fn [response] (error-handler ch response))})
    ch))

(defn do-http-get [url]
  (log (str "GET " url))
  (let [ch (chan 1)]
    (GET url {:handler (fn [response](response-handler ch response))
              :error-handler (fn [response] (error-handler ch response))})
    ch))

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))


(reg-event-db
 :dataresp
 (fn [db [_ current-data-response]]
   ;(update-in db [:data-response] concat  current-data-response)))
   (assoc db :data-response current-data-response)))


(defn read-data-response [response]
  (let [kwresp (walk/keywordize-keys response)]
    (log "READING DATA RESPONSE ")
    (rf/dispatch [:dataresp kwresp])))


(reg-event-db
 :get-data
 (fn [db [_ _]]
  (def data-url (str "https://jsdemo.envdev.io/sse"))
  (log (str "data-url = " data-url))
  (go
     (read-data-response (<! (do-http-get data-url))))
  db))

;(def events (kvlt.core/event-source! url))

(defn listen-events []
  (let [events (kvlt.core/event-source! "https://jsdemo.envdev.io/sse")]
    (go (loop []
          (when-let [event (<! events)]
            ;(log  (nth (:data event) 0))
            (let [sensordata  (JSON/parse (:data event))
            ;(js/eval (reader/read-string (:data event)))
                  ;cnt (count sensordata)
                  ]
              (rf/dispatch [:dataresp (walk/keywordize-keys (js->clj sensordata))])
              ;(log (str "sensordata =-> \n" (count (walk/keywordize-keys (js->clj sensordata))) (js->clj sensordata) "...\n"))
              )
            (recur))))))

(listen-events)
