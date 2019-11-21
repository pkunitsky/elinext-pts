FROM openjdk:11-slim
RUN apt-get update
RUN apt-get install libfreetype6 -y net-tools iputils-ping
RUN apt-get install libfontconfig1 -y net-tools iputils-ping
MAINTAINER elinext
COPY *.jar rest.jar
ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,address=*:5005,suspend=n","-jar","/rest.jar"]
EXPOSE 8081
EXPOSE 5005