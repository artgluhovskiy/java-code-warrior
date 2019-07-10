#!/usr/bin/env sh
while ! (nc -z ${KAFKA_BOOTSTRAP_HOST} ${KAFKA_BOOTSTRAP_PORT}); do
    echo "Trying to connect to Kafka at ${KAFKA_BOOTSTRAP_HOST}:${KAFKA_BOOTSTRAP_PORT}..."
    sleep 10
done
echo ">> connected to Kafka! <<"
java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8096 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /app/logs-aggregator.jar
