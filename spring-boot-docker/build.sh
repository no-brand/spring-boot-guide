#!/bin/bash

docker build -t spring-guide/spring-boot-docker .
docker run -d -p 8080:8080 spring-guide/spring-boot-docker
