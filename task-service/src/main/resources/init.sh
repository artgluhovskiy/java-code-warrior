#!/usr/bin/env sh
echo "Task Service is launching..."
while ! (nc -z ${SERVICE_REGISTRY_HOST} ${SERVICE_REGISTRY_PORT}); do
    echo "Trying to connect to Service Registry at ${SERVICE_REGISTRY_HOST}:${SERVICE_REGISTRY_PORT}..."
    sleep 60
done
echo ">> connected to Service Registry! <<"
while ! (nc -z ${DATABASE_HOST} ${DATABASE_PORT}); do
    echo "Trying to connect to MongoDB at ${DATABASE_HOST}:${DATABASE_PORT}..."
    sleep 60
done
echo ">> connected to MongoDB database! <<"
java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8094 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /app/task-service.jar

