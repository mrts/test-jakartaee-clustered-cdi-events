# jakartaee-clustered-cdi-events testing application

This application is created for testing the
[`jakartaee-clustered-cdi-events`](https://github.com/mrts/jakartaee-clustered-cdi-events)
EJB module functionality. It demonstrates with a Vaadin application how CDI
events can be used within a Jakarta EE environment, showcasing transparent
event broadcasting and consumption in a clustered setup.

## Components

- `Broadcaster`: Manages event listeners and broadcasts messages to all registered consumers according to the Vaadin Broadcaster pattern.
- `Message`: A simple message class marked with `@Clustered` to enable cluster-wide event distribution.
- `MyUI`: A Vaadin UI component that allows users to send messages and view received broadcasts from local and other nodes.

## Building and running

You need Maven and Java 11 JDK to build and run the application.

Build the application WAR with `mvn package` and deploy the resulting WAR to the
application server.

See configuration and running instructions in the
`jakartaee-clustered-cdi-events` project
[README](https://github.com/mrts/jakartaee-clustered-cdi-events#configuration).

## Usage

1. Open <http://localhost:8080/vaadin-app-1.0-SNAPSHOT/> in a web browser.
2. Type a message and click "Broadcast" to send a CDI event.
3. The application displays messages received from the CDI event system.

## License

This project is licensed under the Apache License - see the [LICENSE](LICENSE) file for details.

- Copyright 2024 Mart Sõmermaa.
