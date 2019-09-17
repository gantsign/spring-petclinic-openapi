# OpenAPI Spring Boot PetClinic Demo

[![Build Status](https://travis-ci.com/gantsign/spring-petclinic-openapi.svg?branch=master)](https://travis-ci.com/gantsign/spring-petclinic-openapi)
[![codecov](https://codecov.io/gh/gantsign/spring-petclinic-openapi/branch/master/graph/badge.svg)](https://codecov.io/gh/gantsign/spring-petclinic-openapi)

This project is a fork of the
[ReactJS variant of the Spring Boot PetClinic](https://github.com/spring-petclinic/spring-petclinic-reactjs).
This the primary reason for this fork is to add an [OpenAPI](https://www.openapis.org) specification
for the REST API and generate the Java REST server API and TypeScript client API using the
[OpenAPI Generator](https://openapi-generator.tech). Several other changes were made further
demonstrate best practice. The key changes are listed below:

### Server changes

* [Replace docker-maven-plugin with jib-maven-plugin](https://github.com/gantsign/spring-petclinic-openapi/commit/4acd919cc490e70d94f76b21e01cb298ff40d5c7)

    [jib](https://github.com/GoogleContainerTools/jib) builds more efficient layered Docker images.

* [Replace Maven with Gradle](https://github.com/gantsign/spring-petclinic-openapi/commit/f1bf161318001cdeb40d232c1d0a4f8949a3c0b8)

    [Gradle](https://gradle.org) is better for Spring Boot applications.

* [Auto format to Google Style](https://github.com/gantsign/spring-petclinic-openapi/commit/7d89e2b4d198135c689172a4cb6c4df25883f0aa)

    Get consistent Java formatting regardless of IDE with
    [google-java-format](https://github.com/google/google-java-format).

* [Add Lombok](https://github.com/gantsign/spring-petclinic-openapi/commit/54a62b558096a276580bdc38cb3c44f5c0ca71c4)

    [Project Lombok](https://projectlombok.org) is a great way to reduce boilerplate Java code.

* [Add DTO layer to model](https://github.com/gantsign/spring-petclinic-openapi/commit/8a642912541d24234a5cba773e574b8a90b64dcd)

  Using [MapStruct](https://mapstruct.org) to map between JPA entities and REST DTOs.

* [Clarify REST API for creating and updating entities](https://github.com/gantsign/spring-petclinic-openapi/commit/f1b573711b7c360884b424a0ce6122c6b159c8a8)

    Avoid having IDs repeated in the URL and the body of the request.

* [Change to use SpringBoot BasicErrorController](https://github.com/gantsign/spring-petclinic-openapi/commit/58667284f33488847ed13f077b5959ce5a91ca4d)

    More idiomatic for Spring Boot.

* [Ensure ownerId is correct when accessing pets](https://github.com/gantsign/spring-petclinic-openapi/commit/4af7e0758da1e6483ab7e2f9792319d2e30302dc)

    Protects against bugs and unexpected behavior.

* [Enforce minimum Java unit test coverage](https://github.com/gantsign/spring-petclinic-openapi/commit/7eb2cad02af8b14b87c070e13944e9603a4e1716)

    To encourage tests to be written going forward.

* [Use OpenAPI code generator](https://github.com/gantsign/spring-petclinic-openapi/commit/d0e75b6e8a8339a825647bd6f0ea09c73bd5bd4c)

    [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator) helps your code align to
    the specification.

* [Replace bean validation with Swagger Request Validator](https://github.com/gantsign/spring-petclinic-openapi/commit/4f7106133c0d05cfe0418b52ba0283849e991a61)

    [Swagger Request Validator](https://bitbucket.org/atlassian/swagger-request-validator/src/master/swagger-request-validator-springmvc/)
    validates directly against the schema to reveal schema violations and make them easy to fix.

* [Make Java builds reproducible](https://github.com/gantsign/spring-petclinic-openapi/commit/dd2c3357b08fe5390966bcb45e5897601d1c3ca1)

    Adopting [reproducible builds](https://reproducible-builds.org) allows you to verify your builds
    and reuse Docker layers.

* [Add Google Error Prone](https://github.com/gantsign/spring-petclinic-openapi/commit/33a5d4654e6135a1fd033de4f28c7ade83a783bd)

    [Error Prone](https://errorprone.info) catches bugs during the build.

* [Add Checkstyle to enforce style on Java sources](https://github.com/gantsign/spring-petclinic-openapi/commit/bd3f27df4d62e84b0c7e3b8c9e7bcb261eb61812)

    [Checkstyle](https://checkstyle.org) detects style issues and helps with conformance to
    [Google Java Style](https://google.github.io/styleguide/javaguide.html).

* [Tune JVM args for JUnit](https://github.com/gantsign/spring-petclinic-openapi/commit/d77b440bacf932f6822507bf2fbdeb38c506eb18)

    Don't waste time on expensive JIT optimizations/profiling for short-lived code.

* [Add Nebula Gradle Lint plug-in](https://github.com/gantsign/spring-petclinic-openapi/commit/3ecb2cfdfc1c4341974c9381ba4015541e6cf71b)

    [Gradle Lint Plugin](https://github.com/nebula-plugins/gradle-lint-plugin) detects issues with
    your Gradle configuration (e.g. duplicate classes).

* [Add HTTP ETag support](https://github.com/gantsign/spring-petclinic-openapi/commit/5cb60a3ee549a358d485d0dcf4c84e78e99762eb)

    Enable HTTP caching for GET requests.

* [Add Cache Control headers to REST API](https://github.com/gantsign/spring-petclinic-openapi/commit/d7e9e48e6166cd7a841ac09cfaebd6398d5e0dfc)

    Specify how long responses can be cached for.


### UI changes

* [Auto format to Google TypeScript Style](https://github.com/gantsign/spring-petclinic-openapi/commit/b554446e6e543ec36e06cd8a14fe62d697de9d2e)

    Use [Google TypeScript Style](https://github.com/google/gts) to get great style without bike-shedding.

* [Upgrade react-router from 2.7 to 5.0](https://github.com/gantsign/spring-petclinic-openapi/commit/ab0dcc44da29004c18e89e5b9e7d48db4d30dac7)

    Make navigation simple with [React Router](https://reacttraining.com/react-router/).

* [Switch to create-react-app](https://github.com/gantsign/spring-petclinic-openapi/commit/2554a1de60c3ffade3a20ab37e890aa47f2bbc2d)

    Simplify React build config with [Create React App](https://github.com/facebook/create-react-app).

* [Use generated REST client in React app](https://github.com/gantsign/spring-petclinic-openapi/commit/d2c2815da6b96ccf7d39e31facf3602397a978e1)

    [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator) helps your code align to
    the specification.

* [Change promises to async/await](https://github.com/gantsign/spring-petclinic-openapi/commit/b5607168c0a12ce4dec51d4a6774416ebed148f1)

    [Async/await](https://javascript.info/async-await) allows you to write asynchronous code in
    simple imperative style.

* [Change React forms to use Formik](https://github.com/gantsign/spring-petclinic-openapi/commit/8b0c10caa93f1670ba4b88650f68167db6c71653)

    Use [Formik](https://jaredpalmer.com/formik/) to build forms in React in a simple idiomatic way.

* [Add code-splitting to React app](https://github.com/gantsign/spring-petclinic-openapi/commit/ac0ebda4352f5392cca7413fe6e96cfb86e5959c)

    Reduce initial load times with [Loadable Components](https://github.com/smooth-code/loadable-components).

* [Improve error handling](https://github.com/gantsign/spring-petclinic-openapi/commit/7e9c999b721a96858dc4055652a1714daff3722e)

    Provide a consistent UI for REST API errors.

* [Use React HOC for dependency injection](https://github.com/gantsign/spring-petclinic-openapi/commit/dbdcc551c7e3213ad64943d186a7acfdfa2e36c2)

    Inject REST client using
    [Higher-Order Components](https://reactjs.org/docs/higher-order-components.html) and
    [React Context](https://reactjs.org/docs/context.html).

* [Add Jest Snapshots](https://github.com/gantsign/spring-petclinic-openapi/commit/5f9c05b3153c89202de4419597476246c7f3d4e0)

    [Snapshot Testing](https://jestjs.io/docs/en/snapshot-testing) is a great way to increase test
    coverage for relatively little effort and give protection against unexpected UI changes.

* [Add generic loading component](https://github.com/gantsign/spring-petclinic-openapi/commit/93f10bca7453bc2214bd3f542c814957683b1026)

    Give little UI feedback when the server is taking a while to respond.

### General changes

* [Add Docker config](https://github.com/gantsign/spring-petclinic-openapi/commit/d0e44a78f040b7f8d6867e865b1618fa4289fb76)

    Make it easy to run the app using [Docker Compose](https://docs.docker.com/compose/).

* [Add codecov.io config](https://github.com/gantsign/spring-petclinic-openapi/commit/abb0b83f9eabe688140b05144428deee62cbc382)

    Use [Codecov](https://codecov.io) to track code coverage.

### All changes

[View all the changes](https://github.com/gantsign/spring-petclinic-openapi/compare/69584819f5b384da9353e66d3a9c66dfb9b708a7...master).

## Prerequisites

* Java 8
* NodeJS 10

## Building

To build run the following from the project root folder:

```bash
./build.sh
```

## Running

Note: Spring Boot Server App must be running before starting the client!

To start the server, launch a Terminal and run from the project's root folder:

```bash
./gradlew bootRun
```

When the server is running you can try to access the API for example to query all known pet types:

```bash
curl http://localhost:8080/api/pet-type
```

After starting the server you can run the client from the `client` folder:

```bash
PORT=4444 npm start
```

Note: the Cross-Origin Resource Sharing configuration is set to only permit access to the REST APIs
if the client is run on port `localhost:4444` (this could be changed if necessary).

## Building Docker images

To build run the following from the project root folder:

```bash
./docker-build.sh
```

## Running with Docker Compose

To start the server, launch a Terminal and run from the project's root folder:

```bash
docker-compose up
```

Open your browser to [http://localhost:4444](http://localhost:4444).

## Changes by

John Freeman

GantSign Ltd.
Company No. 06109112 (registered in England)
