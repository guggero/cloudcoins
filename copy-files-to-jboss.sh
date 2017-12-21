#!/bin/bash
# Copy files to jboss & create folders

if [ $# -eq 0 ]
  then
    echo "No arguments supplied... Please provide JBoss home folder"
    echo "Example:"
    echo "      ./copy-files-to-jboss.sh /tmp/wildfly-10.1.0.Final"
fi

if [ $# -eq 1 ]
  then
    JBOSS_HOME_FOLDER=$1
    echo "Using $1 as JBoss home"
    JBOSS_CONFIG_FOLDER=$JBOSS_HOME_FOLDER/standalone/configuration

    mkdir -p $JBOSS_HOME_FOLDER/modules/org/postgresql/jdbc/main
    echo "Created path org/postgresql/jdbc/main in $1/modules"

    MAIN_FOLDER=$JBOSS_HOME_FOLDER/modules/org/postgresql/jdbc/main

    yes | cp -f standalone.xml $JBOSS_CONFIG_FOLDER
    echo "Override $JBOSS_CONFIG_FOLDER/standalone.xml"

    yes | cp -f docker/wildfly/configuration/application.keystore $JBOSS_CONFIG_FOLDER
    yes | cp -f docker/wildfly/configuration/application-users.properties $JBOSS_CONFIG_FOLDER
    yes | cp -f docker/wildfly/configuration/application-roles.properties $JBOSS_CONFIG_FOLDER
    echo "Copied application.keystore, application-users.properties, application-roles.properties to $JBOSS_CONFIG_FOLDER"

    yes | cp -f docker/wildfly/jdbc-driver/module.xml $MAIN_FOLDER
    yes | cp -f docker/wildfly/jdbc-driver/postgresql-9.4.1211.jar $MAIN_FOLDER
    echo "Copied module.xml and postgresql-9.4.1211.jar to $MAIN_FOLDER"
    echo "Files successfully copied!"
fi