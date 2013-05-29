This project is a minimal database-backed webservice with GET, POST, PUT,
and DELETE methods that serves as an illustration of webservice
best practices.  It supports endpoints such as

    GET http://localhost:8080/rest/employee/1
    GET http://localhost:8080/rest/employee?page=3&pageSize=20
    GET http://localhost:8080/rest/employee?name=Alice&page=0&pageSize=10
    PUT http://localhost:8080/rest/employee/1
    POST http://localhost:8080/rest/employee
    DELETE http://localhost:8080/rest/employee/1


It features Spring, Jersey, Metrics, JUnit, Mockito, Cargo, RestAssured, logback, and other technologies.

To do a simple build (with unit tests), producing a war configured to use the JNDI
production data source:

    mvn clean package

To run the webservice without deploying to your own container (useful for manual
testing with local mysql database):

    mvn clean tomcat7:run

To run with integration tests with an embedded tomcat and embedded H2 database:

    mvn clean verify

NOTE: The integration test suite inside the application does not replace
the large regression suite to be built by QA.

See http://zhentao-li.blogspot.com/2011/12/create-maven-archetype-from-existing.html on how to create maven archetype 
from an existing project.

