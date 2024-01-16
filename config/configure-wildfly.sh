#!/bin/bash

set -eu

OTHER_NODE_HOSTNAME=$1

echo "Replacing OTHER_NODE_HOSTNAME with ${OTHER_NODE_HOSTNAME}"

# Replace node hostname marker with the environment variable value passed in via Docker Compose
sed -i "s/OTHER_NODE_HOSTNAME/${OTHER_NODE_HOSTNAME}/g" /opt/jboss/wildfly/wildfly-configuration-commands.cli

# Execute JBoss CLI commands
/opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/jboss/wildfly/wildfly-configuration-commands.cli
