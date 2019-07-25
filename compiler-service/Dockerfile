FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
WORKDIR /usr/app
ADD docker/compiler-service-boot-0.0.1-SNAPSHOT.jar /usr/app/compiler-service.jar
ADD docker/init.sh /usr/app/init.sh
ADD docker/bin/* /usr/app/bin/
EXPOSE 8082 8092
RUN sh -c 'touch compiler-service.jar'
RUN sh -c 'chmod +x init.sh'
ENTRYPOINT ["sh", "-c", "/usr/app/init.sh"]
