/*
 * Copyright 2024 Mart Somermaa
 * SPDX-License-Identifier: Apache-2.0
 */

package org.test;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Route("")
@CdiComponent
@Slf4j
public class MainView extends VerticalLayout {

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
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        final UI ui = attachEvent.getUI();
        Objects.requireNonNull(ui, "UI cannot be null");
        broadcasterRegistration = broadcaster.register(message -> {
                log.info("{} received message: {}", this, message.getText());
                ui.access(() -> Notification.show(message.getText()));
            }
        );
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (broadcasterRegistration != null) {
            broadcasterRegistration.remove();
            broadcasterRegistration = null;
        }
    }

}
