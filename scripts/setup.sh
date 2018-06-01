#!/usr/bin/env bash
set -ex

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
    --name kodiak_helper \
    alpine /bin/sh

docker cp "$config_path" kodiak_helper:/data
docker rm kodiak_helper

docker volume create kodiak_logs

if [ "$VAULT_AUTH_GITHUB_TOKEN" = "" ]; then
    echo -n "Please provide your GitHub personal access token for the vault: "
    read -s token
    echo ""
    export VAULT_AUTH_GITHUB_TOKEN=${token}
fi

# Rewrite the config to only the chosen targets
${HERE}/kodiak init --github-token=${VAULT_AUTH_GITHUB_TOKEN} $targets

# Install kodiak
ln -sf $(realpath ${HERE}/kodiak) /usr/local/bin/kodiak

echo "-----------------------------------------------"
echo "Setup complete. You can now run: "
echo "backup:           Run kodiak backup"
echo "restore:          Run kodiak restore"