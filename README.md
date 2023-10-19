[![Actions Status](https://github.com/domiballoch/bookstoreV2/actions/workflows/maven.yml/badge.svg)](https://github.com/domiballoch/bookstoreV2/actions)
[![codecov](https://codecov.io/gh/domiballoch/bookstoreV2/branch/master/graph/badge.svg?token=3DQWELQG2V)](https://codecov.io/gh/domiballoch/bookstoreV2)
[![GitHub version](https://badge.fury.io/gh/domiballoch%2FbookstoreV2.svg)](https://badge.fury.io/gh/domiballoch%2FbookstoreV2)
[![forthebadge](https://forthebadge.com/images/badges/not-a-bug-a-feature.svg)](https://forthebadge.com)

<h1>Bookstore service</h1>

<h4>Description</h4>
Bookstore with rest functionality using get, post, delete, put
<br/>To view API documentation - run project then visit: http://localhost:8080/bookstoreV2/swagger-ui/index.html

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

<h4>Connection</h4>
Login with root or create user and change yml
<br/>url: jdbc:mysql://localhost:3306/bookstore
<br/>username: root

<h4>Logging level</h4>
info - set filepath as/if required
