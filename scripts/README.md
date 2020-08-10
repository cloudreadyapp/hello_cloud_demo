# cloud-demo-backend Scripts
Backend Scripts for Pixel Journal, a JumpStart Tech Challenge Project.

## System pre-requisites
- Java
- Git
- Maven

# Instruction on how to run the project

# Long way (scripts below)
## Build and run project as docker container
### [1] Install required libraries
```
mvn -f database install
```
### [2] Install required maven packages
```
mvn package
mvn -f database liberty:package
```
### [3] Build and run docker containers as microservices
```
docker pull open-liberty
docker build -t simple_demo database/.
docker run -d --name demo_database -p 9080:9080 -e cloudantapikey="<ask for api key in slack>" simple_demo
```

## [4] Stopping containers
```
docker stop demo_database
```

## [5] Erasing containers and cleaning project
```
docker rm simple_demo
docker image rm --force <imageID>
docker system prune -f
mvn clean
```

# Alternative (recommended) 
## You can run the scripts. Steps [1-3]
```
./scripts/buildAndRunContainers.sh
```

## To stop all containers. Step [4]
```
./scripts/stopContainers.sh
```

## To stop and clean all containers (recommended if you make updates). Steps [4-5]
```
./scripts/cleanContainers.sh
```