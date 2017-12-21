# cloudcoins

[![Build Status](https://travis-ci.org/guggero/cloudcoins.svg?branch=master)](https://travis-ci.org/guggero/cloudcoins)

This is the frontend and backend application for the website [cloudcoins.ch](https://www.cloudcoins.ch).

The project is in a very early stage, so this is work in progress.

## development

### Preparing projects
1. Fork and clone repo
1. Import into IntelliJ
1. Run maven task `install` on projects `cloudcoins-frontend` `cloudcoins-backend`

### Setting up local application server 
1. Download WildFly [10.1.0-Final](http://download.jboss.org/wildfly/10.1.0.Final/wildfly-10.1.0.Final.zip) and extract content
1. Copy files to local WildFly. You can use the script: copy-files-to-jboss.sh
   1. Usage: `./copy-files-to-jboss.sh /absolute/installation/path/wildfly-10.1.0.Final`
1. Add JBoss server in IntelliJ
   1. On top right dropdown -> edit configurations
   1. Add a new one with `+`
   1. Choose local JBoss
   1. Specify JBoss directory
   1. In deployment tab, add artifacts with `+`, choose exploded versions

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

### import test data OTP key

Scan the following QR code with your 2FA App to get OTP passwords for the test account:

[![QR Code](doc/test-account-2fa-qrcode.png)]