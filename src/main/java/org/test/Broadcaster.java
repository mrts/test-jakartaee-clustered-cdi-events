package org.test;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@ApplicationScoped
@Slf4j
public class Broadcaster {

    private final ConcurrentLinkedQueue<Consumer<String>> listeners = new ConcurrentLinkedQueue<>();
    @Resource
    private ManagedExecutorService executorService;

    public void register(Consumer<String> listener) {
        listeners.add(listener);
    }

    public void unregister(Consumer<String> listener) {
        listeners.remove(listener);
    }

    private void onMessage(@Observes Message message) {
        log.info(this + " got message " + message);
        for (final Consumer<String> listener : listeners) {
            executorService.execute(() -> listener.accept(message.getText()));
        }
    }
}
