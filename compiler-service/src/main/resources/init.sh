#!/usr/bin/env sh
java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8090 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /app/compiler-service.jar

