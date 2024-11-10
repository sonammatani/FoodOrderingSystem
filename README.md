Food Ordering System

Requirement: 

All restaurants have menus that list all the items alongside their price.

Users are shown a list of all items available on the system.

Users can select multiple items from this list and proceed to ordering.

An order is deliverable if all the items in the order can be fulfilled by at least one restaurant.

There exists a restaurant selection strategy. This is a configurable tie-breaker strategy which in
case multiple restaurants are offering the same item selects one of them for fulfilling an item in
an order. Thus there could be a strategy that selects a restaurant that is offering an item at a
lower cost and another one could select a restaurant with a higher rating.

Each restaurant has a maximum processing capacity. Beyond that it won’t accept any further
item requests until items which are being processed are dispatched.

Each restaurant takes some time to prepare and dispatch food. Once the item is dispatched the
restaurant informs the system and the processing capacity reservation for the item is released.

Restaurants should be able to register themselves on the system.

Restaurants should be able to update their menu.

Customers should be able to place an order by giving multiple items and quantity details. Ignore
cart management and payment processing for simplicity.
a. Customer here just inputs the item and its quantity,the system depending on the restaurant
selection strategy should select and place the order accordingly
a. Items of the given quantity can be served from multiple restaurants.

Restaurants should not breach their processing capacity.

The restaurant selection strategy should be configurable.

Implement the lower cost restaurant selection strategy.

Concurrency must be taken care of 



Technologies Used :
Backend: Spring Boot 3.3.5 , Java 17
Database: MySQL 8.0.33
Caching: Redis 3.3.5
API Documentation: Swagger
JUnit Testing: JUnit 5, Mockito 
Build Tool: Gradle 8.10.2



Getting Started :
Prerequisites
Java 17 or above
MySQL Database (Running locally or remotely)
Redis 
Gradle for dependency management and building the application.



Intallation and Execution :
Clone the repository
Build and run as spring boot application 



API Endpoints :
After the application is up , open http://localhost:8080/swagger-ui/index.html to view all endpoints






