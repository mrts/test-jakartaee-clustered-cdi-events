FROM quay.io/wildfly/wildfly:26.1.3.Final-jdk11

COPY standalone-full.xml /opt/jboss/wildfly/standalone/configuration/
COPY vaadin-app-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml", "-b", "0.0.0.0"]

