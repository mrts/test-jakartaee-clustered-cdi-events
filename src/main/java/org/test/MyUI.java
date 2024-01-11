package org.test;

import com.vaadin.annotations.Push;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import javax.inject.Inject;
import java.util.function.Consumer;

@CDIUI("")
@Push(transport = Transport.WEBSOCKET_XHR)
public class MyUI extends UI implements Consumer<Message> {

    @Inject
    private Broadcaster broadcaster;
    private Registration broadcasterRegistration;

    private final VerticalLayout layout = new VerticalLayout();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final TextField name = new TextField();
        name.setCaption("Type your message:");

        Button button = new Button("Broadcast");
        button.addClickListener(e -> broadcaster.broadcast(new Message(name.getValue())));

        layout.addComponents(name, button);
        setContent(layout);

        broadcasterRegistration = broadcaster.register(this);
    }

    @Override
    public void detach() {
        super.detach();
        if (broadcasterRegistration != null) {
            broadcasterRegistration.remove();
            broadcasterRegistration = null;
        }
    }

    @Override
    public void accept(Message message) {
        getUI().access(() -> layout.addComponent(new Label("Got message: " + message.getText())));
    }

}
