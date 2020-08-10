# Hello CLoud Demo

---

## Resource
Contains additional fields:
- `"payload"`: payload associated with entry of databse 
- `"random_number"`: just a random number
- `"location"`: Location of the current entry
- `"email"`: email that'll be used as primary key


[Retrieve Resources](#retrieve-resources)

---

# How to send requests to the database

Replace `http://localhost:9080/` with `TBD` if not running the backend locally.

### If running locally follow instructions from [here](https://github.com/bragaigor/hello_cloud_demo/blob/master/README.md). Copy server.env into database/src/main/liberty/config/ or export cloudant api key via
export cloudantapikey="<cloudant API key here>"
### Then follow instructions from [Build and run project](https://github.com/bragaigor/hello_cloud_demo/blob/master/scripts/README.md) or run
./scripts/buildAndRunContainers.sh


For requests, [postman](https://www.postman.com/downloads/) may be helpful!
##### If using postman: when making a request add an extra header field that has Key: "Authorization" and Value: "Bearer `<accessToken>`"

---

## Document

### Store document
To store a document entry, prepare a document entry in JSON format `<DOCUMENT_ENTRY_JSON>`, i.e. `"{\"payload\":\"This is some payload\",\"random_number\":98765,\"location\":\"Miami, USA\",\"email\":\"someEmail@some.ca\"}"`\
User id will be extracted by the backend from the email entry

Run:
```
curl -v -X POST -H "Content-Type: application/json" -d <DOCUMENT_ENTRY_JSON> "http://localhost:9080/HelloCloudDemoProject/demo/database/store/document"
```

For example:
```
curl -v -X POST -H "Content-Type: application/json" -d "{\"payload\":\"This is some payload\",\"random_number\":98765,\"location\":\"Miami, USA\",\"email\":\"someEmail@some.ca\"}" "http://localhost:9080/HelloCloudDemoProject/demo/database/store/document"
```