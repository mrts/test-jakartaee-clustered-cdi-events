package org.test;

import com.vaadin.annotations.Push;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import javax.inject.Inject;
import java.util.function.Consumer;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@CDIUI("")
@Push(transport = Transport.WEBSOCKET_XHR)
public class MyUI extends UI implements Consumer<String> {

    @Inject
    private Broadcaster broadcaster;
    @Inject
    private javax.enterprise.event.Event<Message> messageEvent;

    private final VerticalLayout layout = new VerticalLayout();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final TextField name = new TextField();
        name.setCaption("Type your message:");

        Button button = new Button("Broadcast");
        button.addClickListener(e -> messageEvent.fire(new Message(name.getValue())));

        layout.addComponents(name, button);

        setContent(layout);

        broadcaster.register(this);
    }

    @Override
    public void detach() {
        broadcaster.unregister(this);
        super.detach();
    }

    @Override
    public void accept(String message) {
        getUI().access(() -> layout.addComponent(new Label("Got message: " + message)));
    }

}
