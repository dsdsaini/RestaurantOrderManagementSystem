----------------------------------------------------------------------------------------------------------
Base URL for Branch Controller
----------------------------------------------------------------------------------------------------------

http://localhost:8080/api/branches

Create Branch
API
POST /api/branches

 Input (JSON)
{
  "name": "Connaught Place",
  "location": "Delhi",
  "active": true
}

Success Output (200)
{
  "id": 1,
  "name": "Connaught Place",
  "location": "Delhi",
  "active": true
}


Error Cases
Branch null
{
  "timestamp": "2026-02-18T10:00:00",
  "message": "Branch cannot be null or empty"
}


Method: POST
URL: http://localhost:8080/api/branches
Body: raw → JSON

Get All Branches
API
GET /api/branches

Output (200)
[
  {
    "id": 1,
    "name": "Connaught Place",
    "location": "Delhi",
    "active": true
  },
  {
    "id": 2,
    "name": "Noida Sector 18",
    "location": "Noida",
    "active": false
  }
]

Postman
Method: GET
URL: http://localhost:8080/api/branches

 Get Branch by ID
API
GET /api/branches/{id}

Example
GET /api/branches/1

Success Output
{
  "id": 1,
  "name": "Connaught Place",
  "location": "Delhi",
  "active": true
}

Not Found
{
  "timestamp": "2026-02-18T10:05:00",
  "message": "Branch not found: 1"
}

Postman
Method: GET
URL: http://localhost:8080/api/branches/1

Update Branch Status
API
PUT /api/branches/{id}/status?active=true

Example
PUT /api/branches/1/status?active=false

Success Output
{
  "id": 1,
  "name": "Connaught Place",
  "location": "Delhi",
  "active": false
}

Not Found
{
  "timestamp": "2026-02-18T10:06:00",
  "message": "Branch not found: 1"
}

Postman
Method: PUT
URL: http://localhost:8080/api/branches/1/status
Params:
  active = false

Delete Branch
API
DELETE /api/branches/{id}

Example
DELETE /api/branches/1

Success Output
200 OK
(no body)

Not Found
{
  "timestamp": "2026-02-18T10:07:00",
  "message": "Branch not found: 1"
}

Postman
Method: DELETE
URL: http://localhost:8080/api/branches/1

(Postman Ready import)
{
  "item": [
    {
      "name": "Create Branch",
      "request": {
        "method": "POST",
        "header": [
          { "key": "Content-Type", "value": "application/json" }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"name\": \"Connaught Place\",\n  \"location\": \"Delhi\",\n  \"active\": true\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/branches",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "branches"]
        }
      }
    },
    {
      "name": "Get All Branches",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/branches",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "branches"]
        }
      }
    },
    {
      "name": "Get Branch By Id",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/branches/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "branches", "1"]
        }
      }
    },
    {
      "name": "Update Status",
      "request": {
        "method": "PUT",
        "url": {
          "raw": "http://localhost:8080/api/branches/1/status?active=false",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "branches", "1", "status"],
          "query": [
            { "key": "active", "value": "false" }
          ]
        }
      }
    },
    {
      "name": "Delete Branch",
      "request": {
        "method": "DELETE",
        "url": {
          "raw": "http://localhost:8080/api/branches/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "branches", "1"]
        }
      }
    }
  ]
}


----------------------------------------------------------------------------------------------------------

Base URL MenuItem Controller
----------------------------------------------------------------------------------------------------------

http://localhost:8080/api/menus

Sample MenuItem JSON

(used in examples)

{
  "id": 1,
  "name": "Paneer Butter Masala",
  "price": 320,
  "branchId": 1,
  "type": "VEG",
  "dietType": "JAIN",
  "category": "MAIN_COURSE"
}


Enums:

MenuType  : VEG, NON_VEG
DietType  : JAIN, VEGAN, REGULAR
Category  : STARTER, MAIN_COURSE, DESSERT, BEVERAGE

Add Menu Item
API
POST /api/menus

Input
{
  "name": "Paneer Butter Masala",
  "price": 320,
  "branchId": 1,
  "type": "VEG",
  "dietType": "JAIN",
  "category": "MAIN_COURSE"
}

Output
{
  "id": 1,
  "name": "Paneer Butter Masala",
  "price": 320,
  "branchId": 1,
  "type": "VEG",
  "dietType": "JAIN",
  "category": "MAIN_COURSE"
}

Postman
POST http://localhost:8080/api/menus
Body → raw → JSON

Get Menu by Branch
API
GET /api/menus/branch/{branchId}

Example
GET /api/menus/branch/1

Output
[
  {
    "id": 1,
    "name": "Paneer Butter Masala",
    "price": 320,
    "branchId": 1,
    "type": "VEG",
    "dietType": "JAIN",
    "category": "MAIN_COURSE"
  },
  {
    "id": 2,
    "name": "Veg Biryani",
    "price": 280,
    "branchId": 1,
    "type": "VEG",
    "dietType": "REGULAR",
    "category": "MAIN_COURSE"
  }
]

Postman
GET http://localhost:8080/api/menus/branch/1

Filter Menu — ALL CASES

Endpoint:

GET /api/menus/filter

Case 1 — Type only
GET /api/menus/filter?branchId=1&type=VEG


Output:

[
  {
    "id": 2,
    "name": "Veg Biryani",
    "price": 280,
    "branchId": 1,
    "type": "VEG",
    "dietType": "REGULAR",
    "category": "MAIN_COURSE"
  }
]

Case 2 — Type + DietType
GET /api/menus/filter?branchId=1&type=VEG&dietType=JAIN


Output:

[
  {
    "id": 1,
    "name": "Paneer Butter Masala",
    "price": 320,
    "branchId": 1,
    "type": "VEG",
    "dietType": "JAIN",
    "category": "MAIN_COURSE"
  }
]

Case 3 — Type + Category
GET /api/menus/filter?branchId=1&type=VEG&category=MAIN_COURSE


Output:

[
  {
    "id": 1,
    "name": "Paneer Butter Masala",
    "price": 320,
    "branchId": 1,
    "type": "VEG",
    "dietType": "JAIN",
    "category": "MAIN_COURSE"
  }
]

Case 4 — Type + DietType + Category
GET /api/menus/filter?branchId=1&type=VEG&dietType=JAIN&category=MAIN_COURSE


Output:

[
  {
    "id": 1,
    "name": "Paneer Butter Masala",
    "price": 320,
    "branchId": 1,
    "type": "VEG",
    "dietType": "JAIN",
    "category": "MAIN_COURSE"
  }
]

Bulk Update Menu
API
POST /api/menus/bulk

Input
[
  {
    "id": 1,
    "name": "Paneer Butter Masala",
    "price": 340,
    "branchId": 1,
    "type": "VEG",
    "dietType": "JAIN",
    "category": "MAIN_COURSE"
  },
  {
    "id": 2,
    "name": "Veg Biryani",
    "price": 300,
    "branchId": 1,
    "type": "VEG",
    "dietType": "REGULAR",
    "category": "MAIN_COURSE"
  }
]

Output
[
  {
    "id": 1,
    "name": "Paneer Butter Masala",
    "price": 340,
    "branchId": 1,
    "type": "VEG",
    "dietType": "JAIN",
    "category": "MAIN_COURSE"
  },
  {
    "id": 2,
    "name": "Veg Biryani",
    "price": 300,
    "branchId": 1,
    "type": "VEG",
    "dietType": "REGULAR",
    "category": "MAIN_COURSE"
  }
]

Postman
POST http://localhost:8080/api/menus/bulk


----------------------------------------------------------------------------------------------------------
Base URL Order Controller
----------------------------------------------------------------------------------------------------------

http://localhost:8080/api/orders

Sample Order JSON
{
  "id": 101,
  "branchId": 1,
  "customerName": "Ravi",
  "status": "CREATED",
  "deliveryCharge": 40,
  "totalAmount": 620,
  "items": [
    {
      "menuItemId": 1,
      "quantity": 2,
      "instruction": "Less spicy"
    },
    {
      "menuItemId": 3,
      "quantity": 1,
      "instruction": "No onion"
    }
  ]
}

Create Order
API
POST /api/orders/create

Postman URL
POST http://localhost:8080/api/orders/create?branchId=1&customerName=Ravi&deliveryCharge=40

Body
{
  "items": {
    "1": 2,
    "3": 1
  },
  "instructions": {
    "1": "Less spicy",
    "3": "No onion"
  }
}


Meaning:

MenuItem 1 → qty 2 → "Less spicy"
MenuItem 3 → qty 1 → "No onion"

Success Output
{
  "id": 101,
  "branchId": 1,
  "customerName": "Ravi",
  "status": "CREATED",
  "deliveryCharge": 40,
  "totalAmount": 620,
  "items": [
    {
      "menuItemId": 1,
      "quantity": 2,
      "instruction": "Less spicy"
    },
    {
      "menuItemId": 3,
      "quantity": 1,
      "instruction": "No onion"
    }
  ]
}

Error Cases
Branch not found
{
  "message": "Branch not found: 1"
}

Menu item invalid
{
  "message": "Menu item not found: 3"
}

Empty items
{
  "message": "Order must contain at least one item"
}

Update Order Status
API
PUT /api/orders/{orderId}/status

Postman URL
PUT http://localhost:8080/api/orders/101/status?status=PREPARING

Success Output
{
  "id": 101,
  "branchId": 1,
  "customerName": "Ravi",
  "status": "PREPARING",
  "deliveryCharge": 40,
  "totalAmount": 620,
  "items": [
    {
      "menuItemId": 1,
      "quantity": 2,
      "instruction": "Less spicy"
    },
    {
      "menuItemId": 3,
      "quantity": 1,
      "instruction": "No onion"
    }
  ]
}

Invalid Status
{
  "message": "Invalid order status: PREPARINGG"
}

Order Not Found
{
  "message": "Order not found: 101"
}

 Postman Quick Hits
POST http://localhost:8080/api/orders/create?branchId=1&customerName=Ravi&deliveryCharge=40
Body:
{
  "items": { "1": 2, "3": 1 },
  "instructions": { "1": "Less spicy", "3": "No onion" }
}

PUT http://localhost:8080/api/orders/101/status?status=PREPARING
PUT http://localhost:8080/api/orders/101/status?status=READY
PUT http://localhost:8080/api/orders/101/status?status=DELIVERED
PUT http://localhost:8080/api/orders/101/status?status=CANCELLED




----------------------------------------------------------------------------------------------------------
Base URL PaymentController
----------------------------------------------------------------------------------------------------------

http://localhost:8080/payments

Sample Payment JSON
{
  "id": 501,
  "orderId": 101,
  "method": "UPI",
  "status": "SUCCESS",
  "amount": 620,
  "refundedAmount": 0
}


Enum:

PaymentMethod: UPI, CARD, CASH

Pay for Order
API
POST /payments/{orderId}/{method}

Example
POST /payments/101/UPI

Postman
POST http://localhost:8080/payments/101/UPI

Success Output
{
  "id": 501,
  "orderId": 101,
  "method": "UPI",
  "status": "SUCCESS",
  "amount": 620,
  "refundedAmount": 0
}

Order Not Found
{
  "message": "Order not found: 101"
}

Already Paid
{
  "message": "Payment already completed for order 101"
}

Invalid Method
POST /payments/101/UPII

{
  "error": "Failed to convert value 'UPII' to PaymentMethod"
}

Retry Payment
API
POST /payments/retry/{orderId}/{method}

Example
POST /payments/retry/101/UPI

Postman
POST http://localhost:8080/payments/retry/101/UPI

Success Output
{
  "id": 501,
  "orderId": 101,
  "method": "UPI",
  "status": "SUCCESS",
  "amount": 620,
  "refundedAmount": 0
}

No Failed Payment Found
{
  "message": "No failed payment to retry for order 101"
}

Partial Refund
API
POST /payments/refund/{orderId}?amount=100

Example
POST /payments/refund/101?amount=100

Postman
POST http://localhost:8080/payments/refund/101?amount=100

Success Output
{
  "id": 501,
  "orderId": 101,
  "method": "UPI",
  "status": "PARTIAL_REFUND",
  "amount": 620,
  "refundedAmount": 100
}

Refund > Paid
{
  "message": "Refund amount exceeds paid amount"
}

Order Not Paid
{
  "message": "Cannot refund unpaid order 101"
}

Get Bill
API
GET /payments/bill/{orderId}

Example
GET /payments/bill/101

Postman
GET http://localhost:8080/payments/bill/101

Output
{
  "itemsTotal": 580,
  "tax": 40,
  "deliveryCharge": 40,
  "grandTotal": 660
}

Order Not Found
{
  "message": "Order not found: 101"
}

Postman  Hits
POST http://localhost:8080/payments/101/UPI
POST http://localhost:8080/payments/retry/101/UPI
POST http://localhost:8080/payments/refund/101?amount=100
GET  http://localhost:8080/payments/bill/101
