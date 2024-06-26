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
* Spring Security V5.4.5
* Spring Data MongoDB V3.1.5
* Spring Data JPA V2.7.2
* Spring Cloud Open Feign V3.0.1
* Spring Rest Template
* Spring Kafka V2.7.2
* OAuth JWT V3.19.2
* MongoDB V4.1.1
* PostgreSQL V42.3.6
* Redis V3.8.0/6.1.9(jedis/lettuce)
* Liquibase V4.0.0
* Apache Commons V3.9/4.4
* Apache HttpClient V4.5.13
* Awaitility V4.1.1
* Modelmapper V2.3.8
* Swagger V2.9.2
* Junit V5.7.1
* Mockito V3.6.28
* Testcontainer V1.16.2
* Open API's (email sender; translater)
* SOAP

### Set up project

##### Global preparation for any set up approaches:
* Install Apache Maven on your machine.
* [Don't need to do, if use kubernetes approach] Create your own account in MongoDB Atlas (cloud repository) and make user by name 'admin'. You need to put on your password as MONGO_DB_PASSWORD environment variable, it will be fetched from env to URI during run the project.
* [Don't need to do, if use kubernetes approach] You have to put on your OS some environment variables like: EMAIL_SENDER_API_TOKEN (need account for open API) , TRANSLATE_API_TOKEN (need account for open API) , PG_PASSWORD , PG_USERNAME . Restart your computer after.

##### In order to set up _some single service_ LOCALLY you need to take care about:
* Run abstractCommand `mvn -P local clean install` or `mvn clean install` in current directory in order to build project.
* Install Java 11 on your machine.
* [OPTIONAL. Only for appropriate service] Run PostgreSQL DB on your local machine by abstractCommand `docker run --name postgres-container -e POSTGRES_DB=apiDB -e POSTGRES_USER=[PG_USERNAME] -e POSTGRES_PASSWORD=[PG_PASSWORD] -p 5432:5432 -it postgres` .
* [OPTIONAL. Only for appropriate service] Run Kafka service on your local machine (as an option you can run Kafka brokers by Docker image, in this case you need to have Docker installed) or cloud service (if you try to deploy it in the cloud).
* Go to the necessary service (root directory) and run it by abstractCommand `java -Dspring.profiles.active=qa -jar target/[artifactId]-[version].jar [fully.qualified.package.Application]` (for instance 'java -Dspring.profiles.active=qa -jar target/auth-service-rest-0.0.1-SNAPSHOT.jar org.tms.authservicerest.AuthServiceRestApplication') or 'mvn spring-boot:run'.

##### In order to set up _all project_ LOCALLY by _Docker_ you have to do:
* Run abstractCommand `mvn -P qa clean install` in current directory in order to build project.
* Install Docker on your machine.
* Go to the `kafka-ssl` directory and build and install into your remote Docker repo CA SSL image by abstractCommands `docker build -t [your_docker_id]/kafka-ca-ssl -f Dockerfile.CA.SSL .` and `docker push [your_docker_id]/kafka-ca-ssl` . 
* Replace into docker files from modules: **order** , **event-storage** and into file **Dockerfile.Kafka.SSL** line `FROM alehvilchynski/kafka-ca-ssl as ssl_ca_container` to line with your docker ID like: `FROM [your_docker_id]/kafka-ca-ssl as ssl_ca_container` .
* Run abstractCommand `docker-compose up --build` from root directory in order to build and run all containers.
* [OPTIONAL] Set up remote debug for every module using ports should be copied from **docker-compose.yml** file from root directory (every port overrides 5005). 

##### In order to set up _all project_ LOCALLY by _Kubernetes_ you have to do:
* Run abstractCommand `mvn -P qa clean install` in current directory in order to build project.
* Prepare necessary Docker images in your remote repo (build images for every service and push them to the DockerHub).
* Replace my Docker ID to your for image of every deployment file.
* Install Kubernetes to your computer.
* Install Ingress Nginx project by abstractCommand:
  `kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.5.1/deploy/static/provider/cloud/deploy.yaml`
* Write secret env variables like: MONGO_DB_PASSWORD , EMAIL_SENDER_API_TOKEN , TRANSLATE_API_TOKEN , PG_PASSWORD , PG_USERNAME as a **Secret** object to your local Kubernetes by abstractCommand:
  `kubectl create secret generic [secret_name] --from-literal [key]=[value]`
* Feed all files in dir **'k8s'** for **kubctl** by running abstractCommand:
  `kubectl apply -f k8s`
* [*] Run requests without port value in order to test locally.
* [OPTIONAL] Set up remote debug for every module using ports should be copied from appropriate **\*-node-port.yaml** file from k8s directory.