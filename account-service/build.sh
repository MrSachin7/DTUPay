#!/bin/bash

set -e
echo "Building account service..."
mvn clean package
docker build -t account-service .
