#!/bin/bash

set -e

docker run --rm --detach \
  --publish 27017:27017 \
  --name usage-mongo \
   mongo:4.0.12
