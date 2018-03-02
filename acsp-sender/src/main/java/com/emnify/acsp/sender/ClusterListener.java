package com.emnify.acsp.sender;

import akka.actor.AbstractActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ClusterListener extends AbstractActor {

  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private final Cluster cluster = Cluster.get(getContext().getSystem());

  @Override
  public void preStart() {

    cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class);
    log.info("{} subscribed to cluster events", getSelf());
  }

  @Override
  public void postStop() {

    cluster.unsubscribe(getSelf());
  }

  @Override
  public Receive createReceive() {

    return receiveBuilder()
        .match(MemberUp.class, mUp -> log.info("Member is up: {}", mUp.member()))
        .match(MemberRemoved.class, mRemoved -> log.info("Member is removed: {}",
            mRemoved.member()))
        .build();
  }
}
