# jakartaee-clustered-cdi-events testing application

This application is created for testing the
[`jakartaee-clustered-cdi-events`](https://github.com/mrts/jakartaee-clustered-cdi-events)
EJB module. It demonstrates with a Vaadin application how CDI events can be
transparently broadcasted within a WildFly (or any other Jakarta EE 10) cluster.

## Components

- `Broadcaster`: Manages event listeners and broadcasts messages to all registered consumers according to the Vaadin Broadcaster pattern.
- `Message`: A simple message class marked with `@Clustered` to enable cluster-wide event distribution.
- `MainView`: A Vaadin view that allows users to send messages and view received broadcasts from local and other nodes.

Here's the component diagram:

![Component diagram](doc/component-diagram.png)

## Building and running locally

You need Maven and Java 17 JDK to build and run the application.

First, build and install `jakartaee-clustered-cdi-events` locally as it is not
published in Maven Central yet:

    git clone https://github.com/mrts/jakartaee-clustered-cdi-events
    pushd jakartaee-clustered-cdi-events
    mvn clean install
    popd

Then build the test application WAR with

    mvn clean package -Pproduction

and deploy the resulting WAR to the application server.

See other configuration and running instructions in the
`jakartaee-clustered-cdi-events` project
[README](https://github.com/mrts/jakartaee-clustered-cdi-events#configuration).

## Usage

1. Open the URL <http://localhost:8080/> in two different web browsers or use a
   normal and a private/incognito window in the same browser. This creates two
separate user sessions for testing.
2. Type a message in either browser and click "Broadcast" or "Async broadcast"
   in either browser. This sends a CDI event with the message.
3. Both browsers will display the message received via the WebSocket protocol
   from the CDI event system.

## Running two nodes with Docker

The application can be run in a clustered configuration using the provided
[Docker](Dockerfile) and [Docker Compose](docker-compose.yml) configuration
files by running the following commands after building the application WAR:

    docker compose build
    docker compose up -d

You can view the logs with:

    docker compose logs -f

Open <http://localhost:8080/> and <http://localhost:8081/> side-by-side in
separate web browser windows and send broadcast messages as described above to test
communication between the cluster nodes.

## Cross-bridging JMS on two WildFly servers

In the Docker Compose example cluster, a JMS cross-bridge is used for JMS
clustering between two WildFly server instances. By cross-bridging JMS on these
servers, the messages are replicated from a topic on one server to the same
topic on the other.

The process involves:

1. Setting up a user on both server instances that the JMS connection will use.
2. Configuring a JMS bridge in instance A with the bridge’s source destination
   connecting to instance B.
3. Similarly, configuring the bridge in instance B with the bridge’s source
   destination connecting to instance A.

The source and target destination of the bridge is the `CLUSTER_CDI_EVENTS`
topic used in `jakartaee-clustered-cdi-events`.

The commands for adding the topic, bridge and other required configuration are in
[`config/wildfly-configuration-commands.cli`](config/wildfly-configuration-commands.cli).

## License

This project is licensed under the Apache License - see the [LICENSE](LICENSE)
file for details.

Here's how to apply the license with `addlicense`:

```sh
go install github.com/google/addlicense@latest
~/go/bin/addlicense -c 'Your Name' -s=only -l=apache src/
```
