#!/usr/bin/env bash
set -e

if [[ ($# -lt 2) ]]; then
    echo "Usage: ./setup.sh PATH_TO_CONFIG [TARGET ...]"
    exit -1;
fi

config_path=$1
shift && targets="$@"   # Targets is all the args after the first

HERE=${BASH_SOURCE%/*}

# Create named volume and copy config in
docker volume create kodiak_config

docker run \
    -v kodiak_config:/data \
    --name helper \
    alpine

docker cp "$config_path" helper:/data
docker rm helper

# Now build the kodiak container
${HERE}/build-app.sh

# Rewrite the config to only the chosen targets
${HERE}/kodiak init $targets

# Install kodiak
ln -sf $(realpath ${HERE}/kodiak) /usr/local/bin/kodiak

echo "-----------------------------------------------"
echo "Setup complete. You can now: "
echo "backup:           Run kodiak backup"
echo "restore:          Run kodiak restore"