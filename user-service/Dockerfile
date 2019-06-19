FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
ADD docker/user-service-boot-0.0.1-SNAPSHOT.jar app/user-service.jar
ADD docker/init.sh app/init.sh
EXPOSE 8086 8096
RUN sh -c 'touch app/user-service.jar'
RUN sh -c 'chmod +x app/init.sh'
ENTRYPOINT ["sh", "-c", "./app/init.sh"]