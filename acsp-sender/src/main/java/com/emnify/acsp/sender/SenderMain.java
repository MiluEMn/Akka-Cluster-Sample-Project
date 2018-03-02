package com.emnify.acsp.sender;

import static akka.pattern.Patterns.gracefulStop;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class SenderMain {

  public static void main(String[] args) throws Exception {

    ActorSystem system = ActorSystem.create("acsp");
    ActorRef cListener = system.actorOf(Props.create(ClusterListener.class), "cListener");

    ActorRef router = system.actorOf(FromConfig.getInstance().props(), "messageRouter");
    ActorRef sender = system
        .actorOf(SenderActor.props(system.settings().config().getLong("sender-id"), router));

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      System.out.println("Press ENTER to send a message or type \"STOP\" to shutdown");
      if ("STOP".equalsIgnoreCase(reader.readLine().trim())) {
        break;
      }
      sender.tell("Do something", null);
    }

    system.stop(sender);
    system.stop(router);
    system.stop(cListener);

    try {
      for (ActorRef aRef : new ActorRef[]{sender, router, cListener}) {
        Future<Boolean> stopped = gracefulStop(aRef, Duration.create(5, TimeUnit.SECONDS));
        Await.result(stopped, Duration.create(6, TimeUnit.SECONDS));
      }
    } finally {
      system.terminate();
    }
  }
}
