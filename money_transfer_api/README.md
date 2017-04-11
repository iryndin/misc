# RESTful API for money transfers between internal accounts

## The task

Design and implement a RESTful API (including data model and the backing implementation) 
for money transfers between internal users/accounts.

## Technology

Following technologies are used:
 - Framework: Dropwizard Framework (http://dropwizard.io)
 - Storage: H2 (http://www.h2database.com)
 - Tests: JUnit + Mockito

## Data model

Money is shown as `Money` data type. But for purpose of our demo we will
keep money in `Long` (signed 64 bit integer) data type. It can be as small
 as necessary fraction of real money to keep transaction and balances correct.
E.g. one unit can be equal to 1/100 of one cent.
 
Also, we do not consider multiple currencies, because this should involve 
also up-to-date exchange rates, which is really not necessary for the purposes 
of our demo. So, all accounts and all money transfers are considered to be in 
 one single currency (whatever can it be).

### Account

| Column name | Type | Nullable | Description | Keys |
| --- | --- | --- | --- | --- |
| ID | Long | N | Account ID | PK |
| Balance | Money | N | Account balance |  |
| Create_date | timestamp | N | Account creation datetime |  |
| Update_date | timestamp | N | Account last update datetime |  |

### Transaction

In each Transaction money go from Credit Account to Debit Account

| Column name | Type | Non-null | Description | Keys |
| --- | --- | --- | --- | --- |
| ID | Long | N | Tx ID | PK |
| Account_Debit_ID | Long | N | Debet Account ID | FK to Account.ID |
| Account_Credit_ID | Long | N | Debet Account ID | FK to Account.ID |
| Amount | Money | N | Money amount of Tx |  |
| Create_date | timestamp | N | Tx execution datetime |  |
| Description | Varchar(255) | Y | Money amount of Tx |  |
| Type | Int | N | Tx Type (whatever can it be) |  |

## API

### Get account balance

*GET /balance/{account_id}*

Return:
 
``` json
{
  "status": "OK",
  "data": {
    "balance": 12345
  }  
}

or

{
  "status": "FAIL",
  "errorCode": 12345, 
  "errorMessage": "No such account"
}
```

### Transfer money between accounts

*PUT /transfer/{credit_account_id}/{debit_account_id}*

Body:

``` json
{
  "amount": 12345,
  "type": 11,
  "description": "example tx"
}
```

Return:

``` json
{
  "status": "OK",
  "data": {
    "txId": 12345
  }  
}

or

{
  "status": "FAIL",
  "errorCode": 12345, 
  "errorMessage": "Not enough money on credit account"
}
```

CURL command line: 

```
curl -X PUT --data "{\"amount\": 12345, \"type\": 11, \"description\": \"example tx\"}" -H "Content-Type: application/json"  http://localhost:8080/transfer/1/2
```

### Get transaction object by ID

*GET /transfer/{tx_id}*

Return:
 
``` json
{
  "status": "OK",
  "data": {
    "id": 12345,
    "account_credit_id": 11,
    "account_debit_id": 22,
    "amount": 34324,
    "create_date": 2234234,
    "description": "vvv",
    "type": 555
  }  
}

or

{
  "status": "FAIL",
  "errorCode": 12345, 
  "errorMessage": "No such transaction"
}
```

### Get a history of transactions (may be filtered by credit/debit account and/or date and/or type) ordered by execution date descending

*GET /transfers*

Possible GET parameters. All parameters are optional. 

|Param Name| Description |
| --- | --- |
| credit_account_id| Credit account ID |
| debit_account_id| Debit account ID |
| start_ts | Timestamp, which sets the earliest date of transactions to return. Inclusive. |
| end_ts | Timestamp, which sets the earliest date of transactions to return. Inclusive. |
| type | Transaction type |

Return: 
``` json
{
  "status": "OK",
  "data": [{
    "id": 12345,
    "account_credit_id": 11,
    "account_debit_id": 22,
    "amount": 34324,
    "create_date": 2234234,
    "description": "vvv",
    "type": 555
  }, {
    "id": 12345,
    "account_credit_id": 11,
    "account_debit_id": 22,
    "amount": 34324,
    "create_date": 2234234,
    "description": "vvv",
    "type": 555
  }]
}

or

{
  "status": "FAIL",
  "errorCode": 12345, 
  "errorMessage": "Wrong format of credit_account_id parameter"
}
```
