#!/usr/bin/env bash
set -e

git_id=$(git rev-parse --short=7 HEAD)
git_branch=$(git symbolic-ref --short HEAD)

# Create an image that compiles, tests and dockerises the app
docker build --tag kodiak-build \
	--build-arg git_id=$git_id \
	--build-arg git_branch=$git_branch \
	.

# Run the created image
docker run \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --network=host \
    kodiak-build
