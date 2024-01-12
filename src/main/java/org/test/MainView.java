/*
 * Copyright 2024 Mart Somermaa
 * SPDX-License-Identifier: Apache-2.0
 */

package org.test;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.function.Consumer;

@Route("")
@CdiComponent
public class MainView extends VerticalLayout implements Consumer<Message> {

    @Inject
    private Broadcaster broadcaster;
    private Registration broadcasterRegistration;

    @PostConstruct
    public void init() {
        final TextField textField = new TextField("Type your message:");
        textField.addThemeName("bordered");

        final Button broadcastButton = new Button("Broadcast",
            e -> broadcaster.broadcast(new Message(textField.getValue())));

        final Button asyncBroadcastButton = new Button("Async broadcast",
            e -> broadcaster.broadcastAsync(new Message(textField.getValue())));

        broadcastButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        broadcastButton.addClickShortcut(Key.ENTER);

        add(textField, broadcastButton, asyncBroadcastButton);

        broadcasterRegistration = broadcaster.register(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        if (broadcasterRegistration != null) {
            broadcasterRegistration.remove();
            broadcasterRegistration = null;
        }
    }

    @Override
    public void accept(Message message) {
        getUI().ifPresent(ui -> ui.access(() -> Notification.show(message.getText())));
    }
}
