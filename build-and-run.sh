#!/bin/bash

set -e

pushd facade
./build.sh
popd

pushd token-service
./build.sh
popd

pushd account-service
./build.sh
popd

pushd payment-service
./build.sh
popd

pushd reporting-service
./build.sh
popd

pushd end-to-end-test-service
./deploy.sh
sleep 10  # Wait for 10 seconds before running the tests
./test.sh
popd
