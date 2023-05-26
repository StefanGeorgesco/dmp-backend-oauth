#stage 1
#Start with a base image containing Java runtime
FROM adoptopenjdk/openjdk15:alpine as builder

WORKDIR source

# The application's jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
COPY ${JAR_FILE} application.jar

#unpackage layered jar file
RUN java -Djarmode=layertools -jar application.jar extract

#stage 2
# Java runtime
FROM adoptopenjdk/openjdk15:alpine-jre

#Add volume pointing to /tmp
VOLUME /tmp
WORKDIR /application
#Copy unpackaged application to new container
COPY --from=builder source/dependencies/ ./
COPY --from=builder source/spring-boot-loader/ ./
COPY --from=builder source/snapshot-dependencies/ ./
COPY --from=builder source/application/ ./

#execute the application
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "org.springframework.boot.loader.JarLauncher"]