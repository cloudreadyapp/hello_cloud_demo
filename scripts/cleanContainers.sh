#!/bin/bash

cleanMicroservice() {
    CONTAINER_ID=$(docker ps -a --filter "name=$1" -q)
    if [ -n "$CONTAINER_ID" ]
    then
        echo "Removing container with name $1 and ID $CONTAINER_ID"
        docker rm $CONTAINER_ID
    fi
    imageID=$(docker images $1 -q)
    if [ -n "$imageID" ]
    then
        echo "Removing $1 imageID: $imageID"
        docker image rm --force $imageID
    fi
}

./scripts/stopContainers.sh
cleanMicroservice simple_demo

docker system prune -f
mvn clean
