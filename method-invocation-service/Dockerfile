FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
ADD docker/invocation-service-boot-0.0.1-SNAPSHOT.jar app/invocation-service.jar
ADD docker/init.sh app/init.sh
EXPOSE 8081 8091
RUN sh -c 'touch app/invocation-service.jar'
RUN sh -c 'chmod +x app/init.sh'
ENTRYPOINT ["sh", "-c", "./app/init.sh"]
