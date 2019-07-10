#!/usr/bin/env sh
java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8096 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /app/logs-aggregator.jar
