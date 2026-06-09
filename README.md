This project represents part of transaction system structure, consisting of 3 modules. Eventually, a financial report will be generated based on these modules. The system is inspired by work I've done on my previous company that running PROD servers and has been modified to comply with the company's requirements. The next step, once the 3 modules are completed, will be to generate daily bank transaction reports.

Main Features

-CRUD operations for each initial module: Beginning Balance, Bank Transaction, and Bank Transaction Beginning Balance

-Validation using jakarta.validation and Lombok

-Native SQL queries using @SubSelect, with data filtering using predicates

-Implementation of Spring IoC and Java Stream API

-Docker containerization

-FeignClient-> used for communicate with other API from other project by access the link url of API

Technologies Used

-Java 17 (to align with the current application environment)

-Spring Boot

-Spring Data JPA

-PostgreSQL

-Lombok

-Swagger/OpenAPI (for API documentation)

-Docker (for containerization)

-Maven
