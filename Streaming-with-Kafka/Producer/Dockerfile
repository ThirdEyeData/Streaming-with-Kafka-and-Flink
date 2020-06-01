FROM openjdk:8-jdk
MAINTAINER Third Eye
RUN apt-get update
RUN apt-get install -y maven
COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src
WORKDIR /usr/local/service
RUN mvn clean compile install package
CMD ["java","-jar","target/Producer-1.0-SNAPSHOT.jar"]
