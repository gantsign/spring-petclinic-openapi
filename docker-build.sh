#!/bin/bash
set -e

./gradlew clean build jibDockerBuild --no-daemon

(cd client && docker build -t spring-petclinic-openapi-client .)
