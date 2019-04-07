FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
ADD docker/compiler-service-boot-0.0.1-SNAPSHOT.jar app/compiler-service.jar
ADD docker/init.sh app/init.sh
EXPOSE 8080 8090
RUN sh -c 'touch app/app.jar'
RUN sh -c 'chmod +x app/init.sh'
ENTRYPOINT ["sh", "-c", "./app/init.sh"]
