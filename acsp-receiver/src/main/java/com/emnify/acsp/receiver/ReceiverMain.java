package com.emnify.acsp.receiver;

import static akka.pattern.Patterns.gracefulStop;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class ReceiverMain {

  public static void main(String[] args) throws Exception {

    ActorSystem system = ActorSystem.create("acsp");
    ActorRef receiverSupervisor = system
        .actorOf(Props.create(ReceiverSupervisor.class), "receivers");

    System.out.println("Press ENTER to shutdown");
    System.in.read();

    Cluster cluster = Cluster.get(system);
    cluster.leave(cluster.selfAddress());

    try {
      Future<Boolean> stopped = gracefulStop(receiverSupervisor,
          Duration.create(5, TimeUnit.SECONDS));
      Await.result(stopped, Duration.create(6, TimeUnit.SECONDS));
    } finally {
      system.terminate();
    }
  }
}
