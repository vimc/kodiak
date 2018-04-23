#!/usr/bin/env bash
#Copies test config to /etc/kodiak and must be run as sudo
set -e

here=$(dirname $0)

mkdir -p /etc/kodiak
cp $here/../config/testconfig.json /etc/kodiak/