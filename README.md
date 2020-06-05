## Wantsome - Budget Tracker


### 1. Description

This is an application which allows an user to manage some personal transactions.
Transactions are grouped into different categories.
Supported actions:
 - add/update/delete transactions
 - add/update/delete categories
 - view all transactions and sort them by date
 - view all categories, sort them by id, and filter them by type
 - sort the transactions by date or amount, filter them by type and by date

---
### 2. Setup

No setup is needed, just start the application. On first startup it will create
an empty local database (named 'budget_tracker.db'), where it will save the future data.

Once started, navigate with a web browser at url: http://localhost:4567/main

---
### 3. Technical details

The project includes a web app (started with WebApp class) user interface.

Technologies used to create it:
- main code is written in Java (work with Java of minimum version 8)
- it uses a small embedded database of type SQLite, using SQL and JDBC to
  connect the Java code to it
- it uses Spark micro web framework (which includes an embedded web server, Jetty)
- web pages: using the Velocity templating engine, to separate the UI code 
  from Java code; UI code consists of basic HTML and CSS code (and a little Javascript)
- charts: using JavaScript library Google Charts (https://developers.google.com/chart)
- web services interface: uses REST principles to define the API, and JSON to
  encode requests/responses (using Gson library)
  
- includes some unit tests for DB part (using JUnit library)

Code structure:
- java code is organized in packages by its role, on layers:
  - db - database part, including DTOs and DAOs, as well as the code to init and connect to the db
  - ui - code related to the interface/presentation layer
  - root package - the main classes for the types of interfaces it supports
- web resources are found in main/resources folder:
  - public - static resources to be served by server directly (images, css files)
  - directly in resources - the Velocity templates
  
Note: the focus of this project is on the back-end part, not so much on front-end part.

