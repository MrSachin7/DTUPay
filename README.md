> Learn how to build, deploy and test DTU Pay in any Linux Machine.

## Prerequesites

In order to run this project, the following tools will need to be installed on your machine:

- Java 21. Download it at [https://www.oracle.com/java/technologies/downloads/#java21](https://www.oracle.com/java/technologies/downloads/#java21 "https://www.oracle.com/java/technologies/downloads/#java21")Âand follow the installation instructions provided.
- Docker (version 27+). Follow the instructions at [https://docs.docker.com/engine/install/](https://docs.docker.com/engine/install/ "https://docs.docker.com/engine/install/") to install it on your preferred Linux distribution.
- Maven (Version 3.9.9). Download at [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi "https://maven.apache.org/download.cgi") and follow the installation instructions provided.

## Git repository URL

https://github.com/MrSachin7/DTUPay.git

## Project Setup

1. Clone the project and access it's directory:

   ```sh
   git clone <REPOSITORY_URL>
   cd <REPOSITORY_DIRECTORY>
   ```

2. Make sure all scripts are executable:
   ```sh
   chmod +x build-and-run.sh
   find . -name "*.sh" -exec chmod +x {} \;
   ```

## Build and Run Project

Run the top-level script to build all services:

```sh
./build-and-run.sh
```

This script will automatically:

- Navigate into each microservice folder
- Run the `build.sh` script for each service, building each Java project and it's correspondent Docker image
- Launch all services
- Run the tests

## Jenkins user

username: Huba
password: Teacherpassword123
