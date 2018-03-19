package org.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.BoundedMessageQueueSemantics;
import akka.dispatch.RequiresMessageQueue;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

public class MasterRouter extends AbstractActor implements RequiresMessageQueue<BoundedMessageQueueSemantics>{
    Router router;

    {
        List<Routee> routees = new ArrayList<Routee>();
        for(int i = 0; i < 5; i++){
            ActorRef ref = getContext().actorOf(Props.create(SmsActor.class).withDispatcher("sms-dispatcher"));
            getContext().watch(ref);
            routees.add(new ActorRefRoutee(ref));
        }

        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, sms -> {
                    router.route(sms, getSender());
                })
                .build();
    }
}
