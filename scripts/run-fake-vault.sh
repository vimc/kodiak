#!/usr/bin/env bash

docker run -d --cap-add=IPC_LOCK -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' -p "1234:8200" --name vault_dev vault:0.9.5

# wait for vault to come up
sleep 2

HERE=$(dirname $0)

export VAULT_ADDR='http://127.0.0.1:1234'

vault auth "myroot"
vault auth-enable github
vault write auth/github/config organization=vimc
vault write auth/github/map/teams/development value=standard
vault policy-write standard "$HERE/standard-vault.policy"