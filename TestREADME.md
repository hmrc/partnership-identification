# Partnership Identification Test End-Points

#### GET /income-tax-self-assessment/known-facts/utr/:sautr

---
Stubs the call to DES to retrieve the Partnership Known Facts. The following Feature Switch will need to be enabled: `Use stub for Partnership Known Facts SAUTR call`.

##### Request:
A valid sautr must be sent in the URI:

| SAUTR                 | Expected Response                       | Reason                             |
|-----------------------|-----------------------------------------|------------------------------------|
| "0000000000"          | ```NOT_FOUND(404)```                    | ```UTR Failed```                   |
| "0000000001"          | ```OK(200)```                           | ```Successful Response, no data``` |
 | Any Other Valid SAUTR | ```OK(200)```                           | ```Successful Response```          |

Example response body:

```
{
      "returnType": "P",
      "postCode": "TF34ER",
      "txpName": "Joe Bloggs",
      "address4": "Test street",
      "correspondenceDetails": {
        "corresEveningTelNum": "0123456789",
        "corresMobileNum": "0123456789",
        "corresFaxNum": "0123456789",
        "corresEmailAdd": "joe@bloggs.com",
        "corresPostCode": "AA11AA"
      },
      "actingInCapacityDetails": {
        "aicTaxpayerName": "Joe Bloggs",
        "capTelephoneNum": "0123456789",
        "capEveningTelNum": "0123456789",
        "capMobileNum": "0123456789",
        "capFaxNum": "0123456789",
        "capEmailAdd": "joe@bloggs.com"
      },
      "baseTaxpayerName": "Joe Bloggs",
      "taxpayerContactDetails": {
        "txpTelephoneNum": "0123456789",
        "txpEveningTelNum": "0123456789",
        "txpMobileNum": "0123456789",
        "txpFaxNum": "0123456789",
        "txpEmailAdd": "joe@bloggs.com"
      },
      "basePostCode": "TF3 4ER",
      "commsPostCode": "TF3 4ER",
      "commsTelephoneNum": "0123456789",
      "traderName": "Foo Bar",
      "traderPostCode": "TF3 4ER",
      "dateOfBirth": "2012-12-12"
    }
```

#### POST /cross-regime/register/GRS

Stubs the call to the Register with Multiple Identifiers API to either create/match the business' identifiers with a BPR (Business partner record). The following Feature Switch will need to be enabled: `Use stub for register API`
A valid regime must be sent as a query parameter. The current accepted values are VATC or PPT. For example:

```
/test-only/cross-regime/register/GRS?grsRegime=VATC
```

##### Request:

No body is required for this request.

##### Response:

Based on the sautr entered.

| SAUTR Entered               | Response                                |
|-----------------------------|-----------------------------------------|
| ```0000000002```            | ```BAD_REQUEST with single error```     |
| ```0000000003```            | ```BAD_REQUEST with multiple errors```  |
| ```Any other valid sautr``` | ```OK```                                |


Status: **OK(200)**

Example Response body: 

```
{
"identification":{
                  "idType":"SAFEID",
                  "idValue":"X00000123456789"
                 }
}
```

Status: **BAD_REQUEST(400)**

Example Response body:

```
{
 "code" : "INVALID_PAYLOAD",
 "reason" : "Request has not passed validation. Invalid Payload."
}
```