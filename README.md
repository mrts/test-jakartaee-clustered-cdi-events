# jakartaee-clustered-cdi-events testing application

This application is created for testing the
[`jakartaee-clustered-cdi-events`](https://github.com/mrts/jakartaee-clustered-cdi-events)
EJB module. It demonstrates with a Vaadin application how CDI events can be
transparently broadcasted within a WildFly (or any other Jakarta EE 8) cluster.

## Components

- `Broadcaster`: Manages event listeners and broadcasts messages to all registered consumers according to the Vaadin Broadcaster pattern.
- `Message`: A simple message class marked with `@Clustered` to enable cluster-wide event distribution.
- `MainView`: A Vaadin view that allows users to send messages and view received broadcasts from local and other nodes.

Here's the component diagram:

![Component diagram](doc/component-diagram.png)

## Building and running locally

You need Maven and Java 11 JDK to build and run the application.

First, build and install `jakartaee-clustered-cdi-events` locally as it is not
published in Maven Central yet:

    cd jakartaee-clustered-cdi-events
    mvn clean install

Then build the test application WAR with

    mvn package -Pproduction

and deploy the resulting WAR to the application server.

See other configuration and running instructions in the
`jakartaee-clustered-cdi-events` project
[README](https://github.com/mrts/jakartaee-clustered-cdi-events#configuration).

## Usage

1. Open <http://localhost:8080/> in a web browser.
2. Type a message and click "Broadcast" to send a CDI event.
3. The application displays messages received from the CDI event system.

## Running two nodes with Docker

The application can be run in a clustered configuration using the provided
[Docker](Dockerfile) and [Docker Compose](docker-compose.yml) configuration
files by running the following commands after building the application WAR:

    docker-compose build
    docker-compose up -d

In case `docker-compose build` fails due to BuildKit errors, try building
without BuildKit:

    DOCKER_BUILDKIT=0 docker-compose build

You can view the logs with:

    docker-compose logs -f

## Cross-bridging JMS on two WildFly servers

In the Docker Compose example, a JMS cross-bridge is used for JMS clustering
between two WildFly server instances. By cross-bridging JMS on these servers,
the messages are replicated from a topic on one server to the same topic on the
other.

The process involves:

1. Setting up a user on both server instances.
2. Configuring the `standalone-full.xml` file of instance A to include a JMS
   bridge with the bridge’s target destination connecting to instance B.
3. Similarly, configuring the `standalone-full.xml` on instance B to include
   the bridge with the bridge’s target destination connecting to instance A.

The source and target destination of the bridge is the `CLUSTER_CDI_EVENTS`
topic used in `jakartaee-clustered-cdi-events`.

The commands for adding the topic, bridge and other required configuration are
in `config/wildfly-configuration-commands.cli`.

## License

This project is licensed under the Apache License - see the [LICENSE](LICENSE)
file for details.

Here's how to apply the license with `addlicense`:

```sh
go install github.com/google/addlicense@latest
~/go/bin/addlicense -c 'Your Name' -s=only -l=apache src/
```
