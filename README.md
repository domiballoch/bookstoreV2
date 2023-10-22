[![Actions Status](https://github.com/domiballoch/bookstoreV2/actions/workflows/maven.yml/badge.svg)](https://github.com/domiballoch/bookstoreV2/actions)
[![codecov](https://codecov.io/gh/domiballoch/bookstoreV2/branch/master/graph/badge.svg?token=3DQWELQG2V)](https://codecov.io/gh/domiballoch/bookstoreV2)

<h1>Bookstore service</h1>

<h4>Description</h4>
Bookstore with rest functionality using get, post, delete, put
<br/>Services for books, users, admin, basket and order
<br/>Design enables stock to go up and down per quantity when book(basket item) placed in basket(persisted)
<br/>Order details with user details is then persisted and basket is deleted
<br/>To view API documentation - run project then visit: http://localhost:8080/bookstore/swagger-ui/index.html
<br/>Next task is to create a user login, then React front-end which uses KeyCloak for security

<h4>Prerequisites</h4>
Java 11
<br/>Spring Boot 2.7.5
<br/>Maven 3.6.3
<br/>MySQL 8

<h4>Build project</h4>
mvn clean install
<br/>spring-boot:run

<h4>Server port / context path</h4>
8080/bookstore

<h4>Database schema</h4
execute bookstore.sql
<br/>schema in sql.txt file in resources
<br/>uses JPA bi-directional one-to-many relationships

<h4>Connection</h4>
Login with root or create user and change yml
<br/>url: jdbc:mysql://localhost:3306/bookstore
<br/>username: root

<h4>Logging level</h4>
Uses logback with console and rolling appender to project path
<br/>level: info - set filepath as/if required
