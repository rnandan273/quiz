# quiz

generated using Luminus version "2.9.11.30"

FIXME

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

To build the jar file
    lein uberjar
To run the application
   java -cp target/uberjar/quiz.jar clojure.main -m quiz.core

Open browser to localhost:3000.
You can view the sensor readings. 
The current version shows the latest readings. This can be modified to show the historical readings also.
Latest first order ( This is working but not checked in)

## License

Copyright © 2017 FIXME
