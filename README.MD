# Overview
The aim of this project is to show some of the pitfalls of Spring Boot integration testing.

# Requirements
- JDK 21 or higher
- Docker environment (like Docker Desktop or similar)

# Running test
After you run all the tests in the project `./gradlew clean test` you will find failing tests. 

In order to fix the test some changes are needed - you can search fo TODOs which mark pieces of code which you will need
to alter (eg remove or add).
```kotlin
    @BeforeEach
    fun setUp() {
//        TODO: uncomment next line to fix the tests
//        systemUnderTest.cleanDb()
        restClient = RestClient.create("http://localhost:$serverPort")
    }
```
# Running locally
Use the local profile to run the application. 
```shell
./gradlew bootRun -Dspring.profiles.active=local
```
