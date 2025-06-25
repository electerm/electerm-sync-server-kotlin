# Kotlin Electerm sync server

[![Build Status](https://github.com/electerm/electerm-sync-server-kotlin/actions/workflows/linux.yml/badge.svg)](https://github.com/electerm/electerm-sync-server-kotlin/actions)

A simple electerm data sync server with kotlin.

## Use

```bash
git clone git@github.com:electerm/electerm-sync-server-kotlin.git
cd electerm-sync-server-kotlin

# create env file, then edit .env
cp sample.env .env

## build
gradlew build

## run
gradlew run

# would show something like
# server running at http://127.0.0.1:7837

# in electerm sync settings, set custom sync server with:
# server url: http://127.0.0.1:7837
# Then you can use http://127.0.0.1:7837/api/sync as API Url in electerm custom sync

# JWT_SECRET: your JWT_SECRET in .env
# JWT_USER_NAME: one JWT_USER in .env
```

## Test

```bash
gradlew test
```

## Write your own data store

Just take [src/main/kotlin/ElectermSync/FileStore.kt](src/main/kotlin/ElectermSync/FileStore.kt) as an example, write your own read/write method

## Sync server in other languages

[https://github.com/electerm/electerm/wiki/Custom-sync-server](https://github.com/electerm/electerm/wiki/Custom-sync-server)

## License

MIT
