package org.test;

import com.vaadin.shared.Registration;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
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
        log.debug("{} registering {}", this, listener);
        listeners.add(listener);
        return () -> {
            log.debug("{} unregistering {}", this, listener);
            listeners.remove(listener);
        };
    }

    public void broadcast(M message) {
        messageEvent.fire(message);
    }

    private void onMessage(@Observes M message) {
        log.debug("{} got message {}", this, message);
        for (final Consumer<M> listener : listeners) {
            executorService.execute(() -> listener.accept(message));
        }
    }
}
