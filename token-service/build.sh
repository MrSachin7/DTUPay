#!/bin/bash

set -e
echo "Building token service..."
mvn clean package
docker build -t token-service .
