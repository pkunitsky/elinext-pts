#PTS Microservices

## Build Services 

Build all services and create docker images.

 
**Windows:** ``gradle build-services``


**Unix:** ``./gradlew build-services``

## Start Services

For up all services use next command: ``docker-compose up``


## Config Service
Liquibase settings:
 - gradlew update (perform liquibase script)
 - gradlew validate (first validate your luqibase script)


## Gateway


## Registry Service


## Rest
Structure of rest service:
- core
  - bl
    - appservice
    - builder
    - workflow
  - dal.repository
  - job
  - util
- model
  - data  
  - entity  
  - exception
  - reference
- presentation
  - dto  
  - services 



## UI Service
Structure of ui service:
- core
  - bl
    - workflow
  - dao.repository
- model
    - dto  
- presentation
  - services

## Swagger
URL for access to the swagger: http://rest/swagger-ui.html#/
Token authorization is available for the header: JSESSIONID.