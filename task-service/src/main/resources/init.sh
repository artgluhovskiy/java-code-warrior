#!/usr/bin/env sh
java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8093 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /app/task-service.jar
