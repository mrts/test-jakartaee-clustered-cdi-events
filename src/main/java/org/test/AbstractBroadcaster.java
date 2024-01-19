/*
 * Copyright 2024 Mart Somermaa
 * SPDX-License-Identifier: Apache-2.0
 */

package org.test;

import com.vaadin.flow.shared.Registration;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@Slf4j
abstract class AbstractBroadcaster<M> {

    private final ConcurrentLinkedQueue<Consumer<M>> listeners = new ConcurrentLinkedQueue<>();
    @Resource
    private ManagedExecutorService executorService;
    @Inject
    private Event<M> messageEvent;

    public Registration register(Consumer<M> listener) {
        log.info("{} registering {}", this, listener);
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            return () -> unregister(listener);
        } else {
            throw new IllegalArgumentException("Listener " + listener + " is already registered");
        }
    }

    public void broadcast(M message) {
        messageEvent.fire(message);
    }

    public void broadcastAsync(M message) {
        messageEvent.fireAsync(message);
    }

    private void onMessageAsync(@ObservesAsync M message) {
        onMessage(message);
    }

    private void onMessage(@Observes(during = TransactionPhase.AFTER_SUCCESS) M message) {
        log.info("{} got message {}, listeners are: {}", this, message, listeners);
        for (final Consumer<M> listener : listeners) {
            executorService.execute(() -> {
                try {
                    listener.accept(message);
                } catch (Exception e) {
                    log.error("Error processing message in listener: {}", listener, e);
                }
            });
        }
    }

    private void unregister(Consumer<M> listener) {
        log.info("{} unregistering {}", this, listener);
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        } else {
            log.warn("Listener {} is not registered, cannot unregister", listener);
        }
    }
}
