#!/usr/bin/env bash
set -e

if [[ ($# -lt 1) ]]; then
    echo "Usage: ./setup.sh PATH_TO_CONFIG"
    exit -1;
fi

config_path=$1

HERE=${BASH_SOURCE%/*}

# Create named volume and copy config in
docker volume create kodiak_config

docker run \
    -v kodiak_config:/data \
    --name kodiak_helper \
    alpine /bin/sh

docker cp "$config_path" kodiak_helper:/data
docker rm kodiak_helper

docker volume create kodiak_logs

# Install kodiak
ln -sf $(realpath ${HERE}/kodiak) /usr/local/bin/kodiak

echo "-----------------------------------------------"
echo "Setup complete. You must first run: "
echo "init:             Run kodiak init (--github-token=GITHUB_TOKEN) [TARGETS...]"
echo "Before you can run any other commands, e.g."
echo "backup:           Run kodiak backup"
echo "restore:          Run kodiak restore"