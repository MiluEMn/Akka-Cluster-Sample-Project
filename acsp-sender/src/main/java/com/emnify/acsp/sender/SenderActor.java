package com.emnify.acsp.sender;

import com.emnify.acsp.common.Message;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SenderActor extends AbstractActor {

  private long senderId;
  private long counter = 1L;
  private ActorRef router;
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public SenderActor(long senderId, ActorRef router) {

    this.senderId = senderId;
    this.router = router;
  }

  public static Props props(long senderId, ActorRef router) {

    return Props.create(SenderActor.class, senderId, router);
  }

  @Override
  public void preStart() {

    log.info("Sender actor {} started", senderId);
  }

  @Override
  public void postStop() {

    log.info("Sender actor {} stopped", senderId);
  }

  @Override
  public Receive createReceive() {

    return receiveBuilder()
        .matchAny(object -> router.tell(new Message(counter++, senderId), getSelf()))
        .build();
  }
}
