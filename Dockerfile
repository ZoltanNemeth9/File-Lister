#Because of Podman I had to specify the image path
FROM docker.io/eclipse-temurin:21-jdk
COPY build/libs/*.jar file_lister.jar
ENTRYPOINT ["java", "-jar", "/file_lister.jar"]
