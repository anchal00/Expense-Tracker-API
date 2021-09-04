**Overview : RESTful API for tracking expenses.**
A RESTful API created using Spring Boot. We have used PostgreSQL as the relational database and JdbcTemplate to interact with that. Apart from this, I have used JSON Web Token (JWT) to add authorization. Using JWT, we can protect certain endpoints and ensure that user must be logged-in to access those.

Using this API , clients can create expense categories and add/remove/update transactions for each of the categories separately.
It supports features like total expense record for each categories and searching for categories as well.

**Tech Stack Used :**
1. Java
2. Spring Boot
3. Maven (build tool)
4. PostgreSQL

**Setup and Installation**

1. Clone the repo from GitHub
    git clone git@github.com:anchal00/Expense-Tracker-API.git
2. cd expense-tracker-api
3. Make sure you have PostgreSQL installed on your system.
4. Create database schema

    Use postgres.sql file under **expense-tracker-api/Database/**, for creating all database objects

    run the script using psql client: **psql -U postgres --file postgres.sql**

    Note: If your database is hosted at some cloud platform or if you have modified the SQL script file with some different username and password, update the src/main/resources/application.properties file accordingly:

5. Run the spring boot application
./mvnw spring-boot:run
6. The server will run on port 8080 and hence all enpoints can be accessed starting from http://localhost:8080