/*
 * Copyright 2024 Mart Somermaa
 * SPDX-License-Identifier: Apache-2.0
 */

package org.test;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.enterprise.event.TransactionPhase;

@ApplicationScoped
public class Broadcaster extends AbstractBroadcaster<Message> {

    // CDI event observer methods cannot be generic due to type erasure.
    private void onMessageAsync(@ObservesAsync Message message) {
        onMessageImpl(message);
    }

    private void onMessage(@Observes(during = TransactionPhase.AFTER_SUCCESS) Message message) {
        onMessageImpl(message);
    }

}
