#!/bin/bash

# https://github.com/viclovsky/swagger-coverage

VER=1.3.5

./target/swagger-coverage-commandline-${VER}/bin/swagger-coverage-commandline \
  -s swagger-2.0.json \
  -c swagger-coverage-config.json \
  -i swagger-coverage-output
