package org.akka.app;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Source;
import org.akka.actors.MasterRouter;

import java.util.concurrent.CompletionStage;

public class RunAkkaApp {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("akka-routing-app");

        LoggingAdapter log = Logging.getLogger(system.eventStream(), "routing-app-log");

        ActorMaterializer materializer = ActorMaterializer.create(system);

        ActorRef smsActor = system.actorOf(Props.create(MasterRouter.class).withDispatcher("sms-dispatcher"), "sms-actor");

        log.info("Sending message started {} :");

        final CompletionStage<Done> done = Source.range(0, 100000)
                .runForeach(nbr -> {
                    smsActor.tell("Thanks for registring your mobile Nbr : {} "+nbr, ActorRef.noSender());
                }, materializer);
    }
}
