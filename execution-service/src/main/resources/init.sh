#!/usr/bin/env sh
echo "Execution Service is launching..."
while ! (nc -z ${SERVICE_REGISTRY_HOST} ${SERVICE_REGISTRY_PORT}); do
    echo "Trying to connect to Service Registry at ${SERVICE_REGISTRY_HOST}:${SERVICE_REGISTRY_PORT}..."
    sleep 60
done
echo ">> connected to Service Registry! <<"
java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8093 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /app/execution-service.jar
