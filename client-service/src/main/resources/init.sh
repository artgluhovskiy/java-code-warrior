#!/usr/bin/env sh
echo "Client Service is launching..."
while ! (nc -z ${SERVICE_REGISTRY_HOST} ${SERVICE_REGISTRY_PORT}); do
    echo "Trying to connect to Service Registry at ${SERVICE_REGISTRY_HOST}:${SERVICE_REGISTRY_PORT}..."
    sleep 10
done
echo ">> connected to Service Registry! <<"
java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8095 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /app/client-service.jar
