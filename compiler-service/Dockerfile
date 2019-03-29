FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
ADD docker/compiler-service-boot-0.0.1-SNAPSHOT.jar app.jar
ADD docker/init.sh init.sh
EXPOSE 8080 8090
RUN sh -c 'touch /app.jar'
RUN sh -c 'chmod +x /init.sh'
ENTRYPOINT ["sh", "-c", "./init.sh"]