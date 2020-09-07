# Hello Cloud Demo Step-by-Step Tutorial

This is a step-by-step tutorial on how to build and deploy this project to the cloud

---

# <a name="prereq"></a> Pre-Requisites

## Tools
- Docker
- Java
- Git
- curl
- Maven
- helm. Install helm by following the instructions [here](https://helm.sh/docs/intro/install/)
- IBM Cloud CLI (IBM Cloud command line interface)
- kubectl (Kubernetes command line interface)

## Accounts and Services
- DockerHub Account. Click [here](https://hub.docker.com/) to create account
- IBM Cloud Account. Click [here](https://cloud.ibm.com) to create account
- Cloudant Database
- Kubernetes Cluster

### If you already have the above pre-requisites, you can jump to [Step-by-Step](#stepbystep)

#### Steps 0 through 6 shows how to setup `IBM Cloud CLI`, `kubectl`, `Cloudant Database` and your `Kubernetes Cluster`

---

### 0. Make sure you have the accounts highlighted in [Pre-Requisites](#prereq), (i.e. DockerHub and IBM Cloud Account) and install helm.

### 1. Install IBM Cloud CLI interface ibmcloud 
Note: This instruction assumes you have Mac OSX. Please visit [platforms](https://cloud.ibm.com/docs/cli?topic=cli-install-ibmcloud-cli) and check your OS equivalent steps to install IBM Cloud CLI for your platform.
```
curl -fsSL https://clis.cloud.ibm.com/install/osx | sh
```

### 2. Install Kubernetes service plugin kubectl (might need to run with sudo)
```
ibmcloud plugin install kubernetes-service
```

### 3. <a name="step3"></a>In a folder of your choosing, clone this project
```
git clone https://github.com/cloudreadyapp/hello_cloud_demo.git
cd hello_cloud_demo
```

### 4. Login into your IBM account
```
ibmcloud login
```
Note: If there’s an error message: “You are using a federated user ID, please use one time passcode ( ibmcloud login --sso )” use
```
ibmcloud login --sso
```

### 5.0 Create a Kubernetes cluster (if you don't have one already).
Note: You can create a cluster using the below command, or via the [IBM Cloud Dashboard UI](https://cloud.ibm.com). You can only have 1 Lite Kubernetes cluster running at a time. If you already have one, use the existing one or delete it and create a new one.
```
ibmcloud ks cluster create classic --name hello-cloud-app-cluster
```
Note: Creating a cluster can take about ~20 minutes. The cluster is ready once its state is `normal`.
### 5.1 <a name="checkStatus"></a>To check the status of the cluster, type
```
ibmcloud ks clusters
```

### 6. Create a Cloudant Database

### 6.1 First, create an [IBM Cloudant](https://www.ibm.com/ca-en/cloud/cloudant) instance 
- [IBM Cloud Platform Instructions](https://cloud.ibm.com/docs/Cloudant?topic=Cloudant-creating-an-ibm-cloudant-instance-on-ibm-cloud) or
- [CLI Instructions](https://cloud.ibm.com/docs/Cloudant?topic=Cloudant-creating-an-ibm-cloudant-instance-on-ibm-cloud-by-using-the-ibm-cloud-cli)

### 6.2 Then, create a non-partitioned Cloudant database in your Cloudant instance
- [instructions](https://cloud.ibm.com/docs/Cloudant?topic=Cloudant-using-the-ibm-cloudant-dashboard)

### 6.3 Create `server.env` database constants file
Note: Before continuing with the tutorial, make sure you create a `server.env` file under `database/src/main/liberty/config/server.env` with the appropriate database constants (API Key, Endpoint URL and Database Name) from the Cloudant instance and database you created in step 6.1
```
cloudantapikey=<cloudant-api-key>
cloudantendpoint=https://<cloudant-end-point>-bluemix.cloudantnosqldb.appdomain.cloud
cloudantdbname=cloud-app
```

---

# <a name="stepbystep"></a>Step-by-Step

### Before proceeding, make sure your cluster is in normal state as highlighted in [step 5.1](#checkStatus) above

---

### 7.0 Make sure you're in the top-level repository directory from [step 3](#step3), `hello_cloud_demo`

### 7.1 Install and setup necessary packages
```
mvn -f database install
mvn -f database liberty:package
docker pull open-liberty
```

### 8. Create container image with tag name (database is the microservice we’re creating)
```
docker build -t hello-cloud-app database
```

### 9. <a name="step9"></a>Check docker ‘IMAGE ID’ (3rd column) for repository ‘hello-cloud-app’
```
docker images
```

### 10.0 Login to your docker account
```
docker login
```

### <a name="step101"></a>10.1 Add tag to your image.
Note: `<docker_image_ID>` is obtained from [step 9](#step9)
```
docker tag <docker_image_ID> <dockerHub_username>/hello-cloud-app:1.0-SNAPSHOT
```

### 11. <a name="step11"></a>Push image to DockerHub
Note: Replace `<dockerHub_username>` with your own DockerHub username
```
docker push <dockerHub_username>/hello-cloud-app:1.0-SNAPSHOT
```

# From here on, if your cluster is NOT in normal state you'll become -> 🤯 check [step 5.1](#checkStatus)
Note: Your cluster needs to be in the normal state for you to proceed. If you cluster is in `deploying` or `pending` state, it means that your cluster is still being created.

### 12. Connect kubectl to cluster
```
ibmcloud ks cluster config --cluster hello-cloud-app-cluster
```

### 13. Check cluster nodes
```
kubectl get nodes
```

### 14. Allow to use the ibm-open-liberty chart
```
helm repo add ibm-charts https://raw.githubusercontent.com/IBM/charts/master/repo/stable/
```

### 15. Use Helm to deploy the microservice
Note: Replace `<dockerHub_username>` with your own DockerHub username
```
helm install hello-cloud-app \
    --set image.repository=docker.io/<dockerHub_username>/hello-cloud-app \
    --set image.tag=1.0-SNAPSHOT \
    --set image.pullSecret=default-us-icr-io \
    --set service.name=hello-cloud-app-service \
    --set service.port=9080 \
    --set service.targetPort=9080 \
    --set ssl.enabled=false \
    ibm-charts/ibm-open-liberty
```

## Yay!! Now your Cloud App is available for the world to see 🎉🎊

### ... but how do we access it?

### 16. Get IP address of cluster and take note of Public IP
```
ibmcloud ks workers --cluster hello-cloud-app-cluster
```

### 17. Get port of the deployed service 
```
kubectl get service hello-cloud-app-service -o jsonpath="{.spec.ports[0].nodePort}{'\n'}"
```

### 18. Let's access it! `<publicIP>` was obtained in step 16 and `<port#>` was obtained in step 17 (previous step)
```
http://<publicIP>:<port#>/HelloCloudDemoProject
```

## Congratulations!!
### You have just built and deployed a backend that can be leveraged by frontend apps -- in our example code, we've included a simple frontend as a basic demo.

---

# <a name="stepbystepRedeploy"></a>Step-by-Step to update existing microservice (using helm)

### 19. Follow steps 7 through 12, but make sure that on [step 10.1](#step101) and [step 11](#step11) you change the tag. Instead of using `1.0-SNAPSHOT` use `1.1-SNAPSHOT` or `2.0-SNAPSHOT` for example:
```
docker tag <docker_image_ID> <dockerHub_username>/hello-cloud-app:2.0-SNAPSHOT
docker push <dockerHub_username>/hello-cloud-app:2.0-SNAPSHOT
```

### 20. Use Helm to re-deploy the microservice
```
sudo helm upgrade hello-cloud-app \
    --set image.repository=docker.io/<dockerHub_username>/hello-cloud-app \
    --set image.tag=2.0-SNAPSHOT \
    --set image.pullSecret=default-us-icr-io \
    --set service.name=hello-cloud-app-service \
    --set service.port=9080 \
    --set service.targetPort=9080 \
    --set ssl.enabled=false \
    ibm-charts/ibm-open-liberty
```