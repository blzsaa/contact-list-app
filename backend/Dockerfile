FROM openjdk:8-alpine


# Add the service itself
ARG JAR_FILE

ENV JVM_ARGS=""
ENV APP_ARGS=""

ADD ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT java -jar ${JVM_ARGS} app.jar ${APP_ARGS}
