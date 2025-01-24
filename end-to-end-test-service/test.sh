#!/bin/bash

set -e
echo "Running tests..."
mvn clean install
mvn test
