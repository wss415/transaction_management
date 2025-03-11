# Project Architecture

## Service Description

Create a simple application related to trasaction management within a banking system.The application should enable users
to record,view,and manage financial transactions.

## Monolithic Architecture

### Explicit Architecture

```
transaction
|--report //JMeter Pressure Test Report
|--src/main/java
|--|--com.hsbc.transaction
|--|--|--adapter
|--|--|--|--driving //primary adapter
|--|--|--|--driven  //secondary adapter
|--|--|--application
|--|--|--|--service // other server interface
|--|--|--|--usecase
|--|--|--|--|--command // command usecase
|--|--|--|--|--query // query usecase
|--|--|--common // project common compenent
|--|--|--|--enum
|--|--|--|--exception
|--|--|--domain
|--|--|--|--context // business domain context
|--|--|--|--core
|--src/main/resources
|--|--static	// static resources
|--|--templates	// page resources
|--|--application.yml
|--entrypoint.sh // startup scripts
|--Dockerfile // docker file scripts
|--README.md   // project description
```

### Command and Query Responsibility Segregation

The service uses the CQRS architecture, which separates commands from queries.

* **command** Commands are behaviors that change the state of the system. For example, various POST, PUT, DELETE methods
  of the RESTful API. all the command logic will eventually sink to the domain layer processing, to ensure that the
  modification of the data strictly conforms to the requirements of the business logic.
* **query** query methods are side-effect free and do not affect the state of the system. Therefore, the query logic
  only reaches the application layer and does not descend to the domain layer. The query logic can be implemented using
  various techniques such as join table queries with various SQL.

## Project Dependency

```
org.projectlombok:lombok # use to generate object setter,getter and constructor etc.
```

## Project startup

```shell
# first, enter project root path
cd transaction

# second，run build-startup.sh. 
# execute 'chmod +x build-startup.sh' if necessary
./build-startup.sh
```