#!/usr/bin/env bash
set -e

git_id=$(git rev-parse --short=7 HEAD)
git_branch=$(git symbolic-ref --short HEAD)

# This is the path for teamcity agents. If running locally, pass in your own docker config location
# i.e. /home/{user}/.docker/config.json
docker_auth_path=${1:-/opt/teamcity-agent/.docker/config.json}

# Create an image that compiles, tests and dockerises the app
docker build --tag kodiak-build \
	--build-arg git_id=$git_id \
	--build-arg git_branch=$git_branch \
	.

# Run the created image
docker run \
    -v /dev/urandom:/dev/random
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v $docker_auth_path:/root/.docker/config.json \
    kodiak-build
