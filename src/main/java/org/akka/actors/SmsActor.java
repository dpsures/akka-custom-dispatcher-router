package org.akka.actors;

import akka.actor.AbstractActor;
import akka.dispatch.BoundedMessageQueueSemantics;
import akka.dispatch.RequiresMessageQueue;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SmsActor extends AbstractActor implements RequiresMessageQueue<BoundedMessageQueueSemantics>{

    private LoggingAdapter log = Logging.getLogger(this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, sms -> {
                    log.info("SMS from source system {} : "+sms);
                })
                .build();
    }
}
