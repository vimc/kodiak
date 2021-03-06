# DEPRECATED
[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

# Kodiak
## Usage
### Installation
```
sudo ./scripts/setup.sh PATH_TO_CONFIG TARGETS...
```
This script copies the config file provided into a named docker volume called `kodiak_config`,
builds a new docker image, and installs the `./kodiak` wrapper script to your path as `kodiak`.

### Init
```
kodiak init (--github-token=GITHUB_TOKEN) TARGETS...
```
Filter the config to the specified targets. Only these targets can be interacted with
 in this Kodiak instance. These targets become default in other commands (backup, restore, list).
 A github token must be passed that can be used to authenticate with the vault to retrieve
 secrets for encryption and communication with AWS.

### Restore
```
kodiak restore [--timestamp TIMESTAMP] [TARGETS...]
```

If no timestamp is specified, latest version of each target is restored. If a
timestamp is specified the version with the closest timestamp equal to or after
the specified timestamp is restored. If targets are specified they override the
default targets.

### Status
```
kodiak status
```

Prints something like:
```
No targets are configured. 
Checking remote...
No backups exist remotely.
```

or 

```
3 targets are configured: vault orderly registry
Checking remote...
vault: Last updated 2018-04-18T02:36:18 (1.3MB)
orderly: Last updated 2018-04-15T02:12:40 (427MB)
registry: No backups exist
```

### Versions
```
kodiak list [--limit LIMIT] TARGET
```

Prints something like:
```
Checking remote...
The following versions exist for vault:
2018-04-18T02:36:18
2018-04-04T02:38:45
2018-03-16T02:35:59
2017-12-06T02:36:11
2017-12-05T02:37:15
2017-12-03T02:35:16
2017-12-01T02:36:30
2017-11-22T02:37:26
2017-11-01T02:39:57
2017-09-15T02:40:02
(Maximum 10 shown, use --limit LIMIT to show more)
```

Or maybe:
```
Checking remote...
No backup exists remotely for vault. To run a backup use: kodiak backup vault
```

### Backup
```
kodiak backup [--always] [TARGETS...]
```

Backups up the default or specified targets to the remote store. If `--always`
is NOT specified, only backs up if there is a diff between the local copy and
the remote copy.

## Config
`starport_path` is where Kodiak reads data from when backing up, and writes to
when restoring. `working_path` is where Kodiak stores hashes, as well as the
chunked encrypted files. So for backup files move from `starport_path` to 
`working_path` (encrypted and chunked) and then to S3. On restore the route is
reversed.

`local_path`s in the targets are relative to the `starport_path`.
`vault_address` is the location of the vault where the encryption key and AWS credentials 
are stored.


```
{
    "config": {
        "vault_address": "http://vault:8200",
        "starport_path": "/home/montagu/starport",
        "working_path": "/home/montagu/starport/kodiak"
    },
    "targets": [
        {
            "id": "vault",
            "encrypted": false,
            "remote_bucket": "kodiak-vault",
            "local_path": "vault"
        },
        {
            "id": "orderly",
            "encrypted": true,
            "remote_bucket": "kodiak-orderly",
            "local_path": "orderly"
        }
    ]
}
```

##Development
Run `./gradlew :copyDevProperties` to move a test config file into place. You 
can then just run from within the IDE by setting up a debug configuration with
the appropriate command line arguments.

###Testing
Unit tests can be run from an IDE or on the command line with `gradlew test`.

For testing the logic around connecting to the vault, first run `./scripts/run-fake-vault.sh`
to spin up a local vault instance. After testing destroy the fake vault
with `./scripts/stop-fake-vault.sh`. If running `gradlew test` these scripts will be run automatically.
