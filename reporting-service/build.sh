#!/bin/bash

set -e
echo "Building reporting service..."
mvn clean package
docker build -t reporting-service .
