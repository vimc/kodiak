#!/usr/bin/env bash
set -e

if [[ ($# -eq 0) ]]; then
    echo "Usage: ./setup.sh PATH_TO_CONFIG"
    exit -1;
fi

config_path=$1
HERE=${BASH_SOURCE%/*}

# Copy config
cp "$config_path" ${HERE}/config.json