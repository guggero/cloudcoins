# cloudcoins

[![Build Status](https://travis-ci.org/guggero/cloudcoins.svg?branch=master)](https://travis-ci.org/guggero/cloudcoins)

This is the frontend and backend application for the website [cloudcoins.ch](https://www.cloudcoins.ch).

The project is in a very early stage, so this is work in progress.

## development

### start local database

```bash
docker run -d \
 -e POSTGRESQL_USER=cloudcoins \
 -e POSTGRESQL_PASSWORD=cloudcoins \
 -e POSTGRESQL_DATABASE=cloudcoins \
 -p 5436:5432 \
 --restart always \
 --name cloudcoins_postgres \
 centos/postgresql-95-centos7
```