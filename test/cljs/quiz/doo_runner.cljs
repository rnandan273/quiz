(ns quiz.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [quiz.core-test]))

(doo-tests 'quiz.core-test)

