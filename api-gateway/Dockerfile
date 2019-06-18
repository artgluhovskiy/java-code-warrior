FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
ADD docker/api-gateway-boot-0.0.1-SNAPSHOT.jar app/api-gateway.jar
ADD docker/init.sh app/init.sh
EXPOSE 8080 8090
RUN sh -c 'touch app/api-gateway.jar'
RUN sh -c 'chmod +x app/init.sh'
ENTRYPOINT ["sh", "-c", "./app/init.sh"]