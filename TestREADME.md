# Partnership Identification Frontend Test End-Points

#### GET /income-tax-self-assessment/known-facts/utr/:sautr

---
Stubs the call to DES to retrieve the Partnership Known Facts. The following Feature Switch will need to be enabled: `Use stub for Partnership Known Facts SAUTR call`.

##### Request:
A valid sautr must be sent in the URI:

SAUTR                | Expected Response                       | Reason
---------------------|-----------------------------------------|------------------------------
"0000000000"         | ```NOT_FOUND(404)```                    | ```UTR Failed```
"0000000001"         | ```OK(200)```                           |  ```Successful Response, no data```
Any Other Valid SAUTR| ```OK(200)```                           |  ```Successful Response```


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