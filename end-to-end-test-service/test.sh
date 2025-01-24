#!/bin/bash

set -e
echo "Running tests..."
mvn clean compile generate-sources test
