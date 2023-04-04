FROM openjdk:8-jdk-alpine
ADD target/*.jar /usr/share/ms-account.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/usr/share/ms-account.jar"]