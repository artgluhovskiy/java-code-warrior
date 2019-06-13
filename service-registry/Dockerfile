FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
ADD docker/service-registry-boot-0.0.1-SNAPSHOT.jar app/service-registry.jar
ADD docker/init.sh app/init.sh
EXPOSE 8761 8791
RUN sh -c 'touch app/service-registry.jar'
RUN sh -c 'chmod +x app/init.sh'
ENTRYPOINT ["sh", "-c", "./app/init.sh"]
