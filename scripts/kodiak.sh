#!/usr/bin/env bash
set -e

HERE=$(cd "$(dirname "$1")"; pwd)/$(basename "$1")

docker run \
    -v $HERE/config:/etc/kodiak/ \
    --network=host \
    kodiak "$@"
