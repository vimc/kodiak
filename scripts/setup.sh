#!/usr/bin/env bash
set -e

if [[ ($# -eq 0) ]]; then
    echo "Usage: ./setup.sh PATH_TO_CONFIG"
    exit -1;
fi

config_path=$1
HERE=${BASH_SOURCE%/*}

# Copy config
cp "$config_path" ${HERE}/../config/config.json

${HERE}/build-app.sh

ln -sf $(realpath ${HERE}/kodiak) /usr/local/bin/kodiak

echo "-----------------------------------------------"
echo "Setup complete. You can now: "
echo "backup:           Run kodiak backup"
echo "restore:          Run kodiak restore"