FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/quiz.jar /quiz/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/quiz/app.jar"]
