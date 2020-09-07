# Hello Cloud Demo
A simple full stack cloud app you can reference to build your own cloud ready app!

- Backend (REST API): [database/src/main/java/io/clouddemo/storage/rest](https://github.com/cloudreadyapp/hello_cloud_demo/tree/master/database/src/main/java/io/clouddemo/storage/rest)
- Frontend: [database/src/main/webapp](https://github.com/cloudreadyapp/hello_cloud_demo/tree/master/database/src/main/webapp)

---

## Step-by-step on how to deploy the `hello_cloud_demo` app with IBM Cloud, Docker and Kubernetes:
### [Tutorial](tutorial.md)

---

## How to build and run the project locally

### Pre-requisites
- An account on [IBM Cloud](https://cloud.ibm.com/)
- An [IBM Cloudant](https://www.ibm.com/ca-en/cloud/cloudant) instance - [IBM Cloud Platform Instructions](https://cloud.ibm.com/docs/Cloudant?topic=Cloudant-creating-an-ibm-cloudant-instance-on-ibm-cloud) or [CLI Instructions](https://cloud.ibm.com/docs/Cloudant?topic=Cloudant-creating-an-ibm-cloudant-instance-on-ibm-cloud-by-using-the-ibm-cloud-cli)
- A non-partitioned Cloudant database in your Cloudant instance

#### System pre-requisites
- Docker
- Java
- Git
- Maven

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
docker run -d --name hello_cloud_demo -p 9080:9080 -e cloudantapikey="<insert cloudant api key>" -e cloudantendpoint="<insert cloudant database endpoint url>" -e cloudantdbname="<insert cloudant database name>" simple_database_microservice
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
```
docker rm hello_cloud_demo
docker image rm --force simple_database_microservice
docker system prune -f
mvn clean
```

---

## Making requests to the database

Here are some sample cURL requests to send/retrieve data to/from our database. These requests are based on a locally-run backend (i.e. `localhost:9080`).

You can also use [Hoppscotch](https://hoppscotch.io/) or [Postman](https://www.postman.com/downloads/) to make HTTP requests.

### DemoDocument fields
- `"first_name"`, eg. "Sam"
- `"last_name"`, eg. "Cloud"
- `"email"`, eg. "samcloud@cloudready.app"

### Sample POST request
```
curl -X POST \
  'http://localhost:9080/HelloCloudDemoProject/demo/database/store' \
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

---

## License
Code in this project is licensed under the Apache License, Version 2.0.
View the license [here](LICENSE).
