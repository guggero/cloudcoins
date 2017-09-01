FROM openshift/wildfly-101-centos7

ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
ENV TZ "Europe/Zurich"

ADD wildfly/jdbc-driver/postgresql-9.4.1211.jar /wildfly/modules/org/postgresql/jdbc/main/
ADD wildfly/jdbc-driver/module.xml /wildfly/modules/org/postgresql/jdbc/main/

ADD target/cloudcoins-backend.war /wildfly/standalone/deployments/cloudcoins-backend.war
ADD target/cloudcoins-frontend.war /wildfly/standalone/deployments/cloudcoins-frontend.war

ADD wildfly/configuration /wildfly/standalone/configuration

CMD $STI_SCRIPTS_PATH/run
