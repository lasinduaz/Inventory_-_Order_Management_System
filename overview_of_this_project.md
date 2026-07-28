Category
--------
id
name
description
created_at

Product
--------
id
name
description
price
stock_quantity
sku
category_id
created_at

Customer
--------
id
first_name
last_name
email
phone
created_at

Orders
--------
id
customer_id
order_date
status
total_amount

OrderItem
--------
id
order_id
product_id
quantity
unit_price
subtotal

Category
     │
One-To-Many
     │
Product

Customer
     │
One-To-Many
     │
Orders

Orders
     │
One-To-Many
     │
OrderItem

Product
     │
Many-To-One
     │
OrderItem

src/main/java

com.company.inventory

│
├── config
│
├── controller
│
├── dto
│
├── entity
│
├── exception
│
├── mapper
│
├── repository
│
├── service
│     ├── impl
│
├── util
│
└── InventoryApplication