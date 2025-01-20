#!/bin/bash

set -e
echo "Building payment service..."
mvn clean package
docker build -t payment-service .
