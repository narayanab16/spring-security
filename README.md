# Spring boot 3.x.x mandates JDK 17 or higher

-  Prereqs: Java 18, IntelliJ/Eclipse, Git 
            Have keycloak server up and running, confidential credential client registered
            Resource server added to the application
            Token able to retrieve from keycloak and the same token passed to the rest api request as a authorization bearer token
-  SpringSecurityApplication - run as java app
- **Note**: Without token the rest end points cant be possible to test

Get home REST API:
- http://localhost:10000/springsecurity

Create a user REST API, I used **postman**/RestClient:
- HTTP method: POST
- URL : http://localhost:10000/springsecurity/addUser
- Header: Content-Type: application/json
- raw Body: {"id":100, "username": "Narayana"}

Get User By ID REST API, I used **postman**/RestClient:
- http://localhost:10000/springsecurity/getUserById/100

Get Users REST API, I used **postman**/RestClient:
- http://localhost:10000/springsecurity/getUsers
