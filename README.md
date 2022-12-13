## Mutual Aid App

That project is C2C application. One of user (Customer) can publish certain Job. From other hand another users (Workers) can apply to that Job and accomplish it. 

### Modules
* common-http-autoconfiguration - the custom Spring Boot starter which adds to every module opportunity to put language to request as header and inject that data as bean in every place in module.
* exception-handling-autoconfiguration - the general place to handle all exceptions according request language.
* model-mapper-autoconfiguration - the custom mapper with improved opportunity (additional methods, auto registration for converters)
* auth-service-client - it generates auth service models (API layer) by swagger-codegen-maven-plugin.
* auth-configuration - security configuration. Support basic authentication (for other module-services) and by JWT token (for users). Provides module-services with method security level. 
* auth-service-rest - service which keeps and operates authorisation data for all users.
* profile-service-client - it generates profile service models (API layer) by swagger-codegen-maven-plugin.
* profile-service-rest - service which keeps and operates user account information for all users.
* order-service - service which is responsible for all operations under orders from Customer as well as Worker sides.
* event-storage-service - that service gets (asynchronously) and keeps certain events of order live cycle.
* message-chat-service - provide users with opportunity to communicate each other within my application. As additional feature there is embedded translater.
* task-executor-service - reruns failed tasks/request by rest calls or scheduled jobs.
* thread-save - starter for making certain resource (service) thread-save. 

### Technologies
* Java 11
* Spring Boot V2.4.3
* Apache Commons V3.9/4.4
* Swagger V2.9.2
* Junit V5.7.1
* Mockito V3.6.28
* Testcontainer V1.16.2
* Spring Security V5.4.5
* Apache HttpClient V4.5.13
* OAuth JWT V3.19.2
* MongoDB V4.1.1
* Spring Data MongoDB V3.1.5
* Spring Cloud Open Feign V3.0.1
* Spring Kafka V2.7.2
* Awaitility V4.1.1
* Modelmapper V2.3.8
* Open API's (email sender; translater)

### Set up project

In order to set up project you need to take care about:
* Install Java 11, Apache Maven on your machine.
* Create your own account in MongoDB Atlas (cloud repository) and make user by name 'admin'. You need to put your password as environment variable, it will be fetched from env to URI during run the project. 
* Run Kafka service on your local machine (as an option you can run Kafka brokers by Docker image, in this case you need to have Docker installed) or cloud service (if you try to deploy it in the cloud).
* Set up and run SSL secured Kafka connections (set up/run brokers and set up certificates for application.yml). In order to do that you need to regard file by path './kafka-ssl/README.md'.
* You need to put as environment variable access tokens for open API's like EMAIL_SENDER_API_TOKEN and TRANSLATE_API_TOKEN.

After that preparations you can run command 'mvn clean install' in current directory in order to build project.

After that you can go to needed service (root directory) and run it by command 'java -jar target/[artifactId]-[version].jar [fully.qualified.package.Application]' (for instance 'java -jar target/auth-service-rest-0.0.1-SNAPSHOT.jar org.tms.authservicerest.AuthServiceRestApplication') or 'mvn spring-boot:run'.