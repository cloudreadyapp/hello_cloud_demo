#!/bin/bash

stopContainers() {
    isRunning=$(docker inspect -f '{{.State.Running}}' $1 2>&1)
    wait
    if [ "$isRunning" == "true" ]
    then
        echo "Stopping $1 container..."
        docker stop $1
    else
        echo "Nothing to do for $1 (container already stopped)"
    fi
}

stopContainers demo_database
