FROM openjdk:8-jdk-alpine
MAINTAINER gluhovskiy.art@gmail.com
ADD docker/logs-aggregator-boot-0.0.1-SNAPSHOT.jar app/logs-aggregator.jar
ADD docker/init.sh app/init.sh
EXPOSE 8086 8096
RUN sh -c 'touch app/logs-aggregator.jar'
RUN sh -c 'chmod +x app/init.sh'
ENTRYPOINT ["sh", "-c", "./app/init.sh"]
