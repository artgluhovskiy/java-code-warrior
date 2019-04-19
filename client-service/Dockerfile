FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
ADD docker/client-service-boot-0.0.1-SNAPSHOT.jar app/client-service.jar
ADD docker/init.sh app/init.sh
EXPOSE 8082 8092
RUN sh -c 'touch app/client-service.jar'
RUN sh -c 'chmod +x app/init.sh'
ENTRYPOINT ["sh", "-c", "./app/init.sh"]