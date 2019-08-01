#!/bin/bash

set -e

RUN_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $RUN_PATH

PROJECTS="usage-metrics-agent usage-metrics-central"

for PROJECT in $PROJECTS; do

	echo $PROJECT
	
	echo ----[ Upload docker image ]----
	DOCKER_IMAGE=$PROJECT:$VERSION
	docker login
	docker tag $DOCKER_IMAGE foilen/$DOCKER_IMAGE
	docker tag $DOCKER_IMAGE foilen/$PROJECT:latest
	docker push foilen/$DOCKER_IMAGE
	docker push foilen/$PROJECT:latest

done
