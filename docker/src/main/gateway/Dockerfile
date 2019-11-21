FROM openjdk:11-slim
MAINTAINER elinext
COPY *.jar gateway.jar
ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,address=*:5006,suspend=n","-jar","/gateway.jar"]
EXPOSE 8097
EXPOSE 5006