
# Partnership Identification

### How to run the service
1. Make sure any dependent services are running using the following service-manager command `sm --start PARTNERSHIP_IDENTIFICATION_ALL -r`
2. Stop the frontend in service manager using `sm --stop PARTNERSHIP_IDENTIFICATION`
3. Run the frontend locally using
   `sbt 'run 9987 -Dapplication.router=testOnlyDoNotUseInAppConf.Routes'`
   
### Testing
See the TestREADME for more information on how to use our stubs for testing

### End-Points
#### POST /journey

---
Creates a new journeyId and stores it in the database

##### Request:
No body is required for this request

##### Response:
Status: **Created(201)**

Example Response body:

```
{“journeyId”: "<random UUID>"}
```

#### GET /journey/:journeyId

---
Retrieves all the journey data that is stored against a specific journeyID.

##### Request:
A valid journeyId must be sent in the URI

##### Response

| Expected Status                         | Reason
|-----------------------------------------|------------------------------
| ```OK(200)```                           |  ```JourneyId exists```
| ```NOT_FOUND(404)```                    | ```JourneyId does not exist```

Example response body:
```
{
"sa-utr": "1234567890",
"postcode":"AA11AA"
}
```

#### GET /journey/:journeyId/:dataKey

---
Retrieves all the journey data that matches the dataKey for a specific journeyID.

##### Request:
Example Request URI

`testJourneyId = <random UUID>`
```
/journey/testJourneyId/postcode
```

##### Response:

| Expected Status                         | Reason
|-----------------------------------------|------------------------------
| ```OK(200)```                           |  ```JourneyId exists```
| ```NOT_FOUND(404)```                    | ```No data exists for JourneyId or dataKey```
| ```FORBIDDEN(403)```                    | ```Auth Internal IDs do not match```


Response body for example URI:
```
{"AA11AA"}
```

#### PUT /journey/:journeyId/:dataKey

---
Stores the json body against the data key and journey id provided in the uri

##### Request:
Requires a valid journeyId and user must be authorised to make changes to the data

Example request URI:
`testJourneyId = <random UUID>`
```
/journey/testJourneyId/postcode
```

Example request body:
```
{"AA11AA"}
```
##### Response:

| Expected Status                         | Reason
|-----------------------------------------|------------------------------
| ```OK(200)```                           |  ```OK```
| ```FORBIDDEN(403)```                    | ```Auth Internal IDs do not match```

#### DELETE /journey/:journeyId/:dataKey

---
Removes the data that is stored against the dataKey provided for the specific journeyId

##### Request:
Requires a valid journeyId and dataKey

Example request URI:
`testJourneyId = <random UUID>`
```
/journey/remove/testJourneyId/postcode
```

##### Response:

| Expected Status                         | Reason
|-----------------------------------------|------------------------------
| ```NO_CONTENT(204)```                   |  ```Field successfully deleted from database```
| ```INTERNAL_SERVER_ERROR(500)```        | ```Internal ID could not be retrieved from Auth```


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
