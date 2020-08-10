# hello-cloud-demo-backend
Hello Cloud App, A Skeleton to help you build your cloud ready backend for your app

## System pre-requisites
- Java
- Git
- Maven

# How to run the project

## Clone or Fork the project
```
git clone https://github.com/bragaigor/hello_cloud_demo.git
cd hello_cloud_demo
# put server.env file in database/src/main/liberty/config/ directory
# or export cloudantapikey via
export cloudantapikey=<key>
```

## Build and run the project (for long instruction check README from scripts folder)
```
./scripts/buildAndRunContainers.sh
```

## To stop all containers.
```
./scripts/stopContainers.sh
```

## To stop and clean all containers (recommended if you make updates).
```
./scripts/cleanContainers.sh
```

## To view microservice output as it occurs, take note of the microservice's container name or id and run:
```
docker logs -f <CONTAINER_ID_OR_CONTAINER_NAME>
```
For example,
```
docker logs -f demo_database
```
Use CTRL+C to quit viewing the logs.

## Testing the microservices. Once the containers are up and running do:
```
cd test
mvn liberty:dev
```

##### And press enter

#### If you want to use browser or curl you'll need to include an extra header parameter e.g.
```
curl http://localhost:9080/HelloCloudDemoProject/demo/database/retrieve/{email}
```

#### If using postman: when making a request add an extra header field that has Key: "Authorization" and Value: "Bearer `<accessToken>`"
- http://localhost:9080/HelloCloudDemoProject/demo/database/retrieve/{email}

--- 
# Interacting with the database
#### Refer to the instructions [here](exampleJSON/README.md).
