(ns quiz.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [quiz.ajax :refer [load-interceptors!]]
            [quiz.handlers]
            [quiz.subscriptions])
  (:import goog.History))


(defn log [s]
  (.log js/console (str s)))

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))

(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:nav.navbar.navbar-dark.bg-primary
     [:button.navbar-toggler.hidden-sm-up
      {:on-click #(swap! collapsed? not)} "☰"]
     [:div.collapse.navbar-toggleable-xs
      (when-not @collapsed? {:class "in"})
      [:a.navbar-brand {:href "#/"} "quiz"]
      [:ul.nav.navbar-nav
       [nav-link "#/" "Home" :home collapsed?]]]]))



(defn home-page []
  (let [data-orig  (rf/subscribe [:data-response])]
  (fn []
    (let [data (reverse @data-orig)]
    [:div 
      [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "center" :margin-left "20px"}}
       [:h2 "Sensor data "]
      ]
      (doall      
        (for [idx (range (count data))]
              ^{:key (str "at3 -" (rand 10000000))}
           [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "center" :margin-left "20px"}}
               [:div {:style {:flex 1}} [:h5 (str (:name (nth data idx)))]]
               [:div {:style {:flex 1}} [:h5 (str (:unit (nth data idx)))]]
               [:div {:style {:flex 3}} [:h5 (str (:measurements (nth data idx)))]]
           ]))]))))

(def pages
  {:home #'home-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
