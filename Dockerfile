FROM maven:3.8.1-jdk-11 AS build

# set working directory in container
WORKDIR /app

COPY ./myachef-dao ./myachef-dao
COPY ./myachef-business ./myachef-business
COPY ./myachef-ws ./myachef-ws
COPY ./pom.xml ./pom.xml

# build project using maven
RUN mvn clean package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

COPY --from=build ./app/myachef-ws/target/*.war myachef.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","myachef.war"]