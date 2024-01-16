FROM quay.io/wildfly/wildfly:26.1.3.Final-jdk11

ARG OTHER_NODE_HOSTNAME

COPY config/wildfly-configuration-commands.cli /opt/jboss/wildfly/
COPY config/configure-wildfly.sh /opt/jboss/wildfly/

RUN /opt/jboss/wildfly/configure-wildfly.sh $OTHER_NODE_HOSTNAME

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
RUN /opt/jboss/wildfly/bin/add-user.sh -a jmsuser jmspass --group guest --silent

COPY target/vaadin-app-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml", "-b", "0.0.0.0"]

