#!/bin/bash

BUILDLOGFILE=cloudDemoBuild.log

installPackages() {
    mvn -f $1 install
    mvn -f $1 liberty:package
}

buildContainers() {
    imageID=$(docker images $1 -q)
    if [ -n "$imageID" ]
    then
        echo "Image $1 already exists with ID: $imageID, skipping..."
    else
        echo "Build container $1"
        docker build -t $1 $2/. >> $BUILDLOGFILE &
    fi
}

startContainers() {
    isRunning=$(docker inspect -f '{{.State.Running}}' $2 2>&1)
    if [ $isRunning == "true" ]
    then
        echo "Container $2 already running. To stop run ONE of the following"
        echo "docker stop $2"
        echo "./scripts/cleanContainers.sh"
        echo "./scripts/stopContainers.sh"
    else
        CONTAINER_ID=$(docker ps -a --filter "name=$2" -q)
        if [ -n "$CONTAINER_ID" ]
        then
            echo "Container $2 already exists, starting container with ID: $CONTAINER_ID"
            docker start $CONTAINER_ID
        else
            echo "Creating new container named: $2"
            # Run container detached (in the background)
            docker run -d --name $2 -p $1:9080 $4 $3 &
        fi
    fi
}

checkContainer() {
    CONTAINER_ID=$(docker ps -f "name=$1" -q)
    if [ -n "$CONTAINER_ID" ]
    then
        echo -e "\t$1 :: \x1b[1;32mRUNNING SUCCESS\x1b[0m :: ID: $CONTAINER_ID"
    else
        echo -e "\t$1 :: \x1b[1;31mERROR\x1b[0m"
        echo "\tPlease check $BUILDLOGFILE for error messages"
    fi
}

SERVERENVFILE="$(pwd)/database/src/main/liberty/config/server.env"
if [ -z "$cloudantapikey" ] && [ ! -f "$SERVERENVFILE" ];
then
      echo "server.env file does not exist and \$cloudantapikey is empty, please export database key through"
      echo " File: $SERVERENVFILE does not exists"
      echo "export cloudantapikey=<key>"
      echo "Or create file server.env with database key inside database/src/main/liberty/config/"
else
    rm -f $BUILDLOGFILE
    installPackages database
    # To build another microservice add it here
    mvn package
    docker pull open-liberty
    echo "----------------------------------------------------------------------------------------"
    echo "--------------------------------[ Start Microservices ]---------------------------------"
    echo "----------------------------------------------------------------------------------------"
    # Build containers
    buildContainers simple_demo database
    # To build another microservice add it here
    sleep 0.1
    echo "Building containers..."
    wait
    # Start database microservice
    startContainers 9080 demo_database simple_demo "-e cloudantapikey=$cloudantapikey"
    # Start another microservice
    echo "Waiting for database container to start..."
    wait
    echo "Microservice status:"
    checkContainer demo_database

    echo "Build log stored at $BUILDLOGFILE"
fi