#!/bin/bash

set -e

RUN_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

PROJECTS="usage-metrics-agent usage-metrics-central"

for PROJECT in $PROJECTS; do

	echo $PROJECT
	cd $RUN_PATH/$PROJECT
	
	echo ----[ Prepare folder for docker image ]----
	DOCKER_BUILD=$RUN_PATH/$PROJECT/build/docker
	
	rm -rf $DOCKER_BUILD
	mkdir -p $DOCKER_BUILD/app
	
	cp -v build/distributions/$PROJECT-$VERSION.zip $DOCKER_BUILD/app/$PROJECT.zip
	cp -v docker-release/* $DOCKER_BUILD
	
	cd $DOCKER_BUILD/app
	unzip $PROJECT.zip
	rm $PROJECT.zip
	mv $PROJECT-$VERSION/* .
	rm -rf $PROJECT-$VERSION
	
	echo ----[ Docker image folder content ]----
	find $DOCKER_BUILD
	
	echo ----[ Build docker image ]----
	DOCKER_IMAGE=$PROJECT:$VERSION
	docker build -t $DOCKER_IMAGE $DOCKER_BUILD
	
	rm -rf $DOCKER_BUILD

done
