#!/bin/bash

set -e
echo "Building facade..."
mvn clean package
docker build -t facade .
