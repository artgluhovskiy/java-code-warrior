FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
ADD docker/execution-service-boot-0.0.1-SNAPSHOT.jar app/execution-service.jar
ADD docker/init.sh app/init.sh
EXPOSE 8083 8093
RUN sh -c 'touch app/execution-service.jar'
RUN sh -c 'chmod +x app/init.sh'
ENTRYPOINT ["sh", "-c", "./app/init.sh"]
