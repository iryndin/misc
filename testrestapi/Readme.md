## The task goal
Execution of "gradle build" have to be successfully completed (passing test phase also).

### Description
A user submits a search query to find an available domain name.

The system should check domain availability with in all supported TLD zones (.com/.net/.club) and return the results among with the registration price for each zone.

A domain is available for registration if it cannot be found in `domains` database table.

Prices for each zone are stored in `tlds` database table.

Request format: `/domains/check?search=new-domain`

Response format:
```
[
{"domain": "new-domain.com", tld: "com", "available": false, "price": 8.99},
{"domain": "new-domain.net", tld: "net", "available": true, "price": 9.99},
{"domain": "new-domain.club", tld: "club", "available": true, "price": 15.99}
]
```
Response items have to be sorted by price ascending (tld with low price at first)

.
