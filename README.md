# hello-cloud-demo-backend
Hello Cloud App, a skeleton to help you build your cloud ready backend for your app.

## System pre-requisites
- Java
- Git
- Maven
- Docker

---

## How to build and run the project locally

### Clone or Fork the project
```
git clone https://github.com/cloudreadyapp/hello_cloud_demo.git
cd hello_cloud_demo
```

### Build and run project as docker container

#### [1] Install required libraries
```
mvn -f database install
```

#### [2] Install required maven packages
```
mvn -f database liberty:package
```

#### [3] Build and run docker container as a microservice
```
docker pull open-liberty
docker build -t simple_database_microservice database
docker run -d --name hello_cloud_demo -p 9080:9080 -e cloudantapikey="<insert cloudant api key>" simple_database_microservice
```

Take note of the outputted docker `<imageID>` so you can delete it later once you're done.
If you lose it, you can do `docker ps -a` to list all running docker images.

#### [4] View the running app!
http://localhost:9080/HelloCloudDemoProject

#### [5] Stop container
```
docker stop hello_cloud_demo
```

#### [6] Erase containers and clean project
Grab the docker `<imageID>` of simple_database_microservice by running `docker images`.

```
docker rm hello_cloud_demo
docker image rm --force <imageID>
docker system prune -f
mvn clean
```

---

## Making requests to the database

Here are some sample cURL requests to send/retrieve data to/from our database. These requests are based on a locally-run backend (i.e. `localhost:9080`).

You can also use [Hoppscotch](https://hoppscotch.io/) or [postman](https://www.postman.com/downloads/) to make HTTP requests.

### DemoDocument fields
- `"first_name"`, eg. "Sam"
- `"last_name"`, eg. "Cloud"
- `"email"`, eg. "samcloud@cloudready.app"

### Sample POST request
```
curl -X POST \
  'http://localhost:9080/HelloCloudDemoProject/demo/database/store/document' \
  -H 'Content-Type: application/json; charset=utf-8' \
  -d '{
    "first_name": "Sam",
    "last_name": "Cloud",
    "email": "samcloud@cloudready.app"
  }'
```

### Sample GET request
```
curl -X GET \
  'http://localhost:9080/HelloCloudDemoProject/demo/database/retrieve?first_name=Sam&last_name=Cloud'
```
Response:
```
{"email":"samcloud@cloudready.app","first_name":"Sam","last_name":"Cloud"}
```
