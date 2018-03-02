package com.emnify.acsp.receiver;

import com.emnify.acsp.common.Message;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ReceiverActor extends AbstractActor {

  private long receiverId;
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public ReceiverActor(long receiverId) {

    this.receiverId = receiverId;
  }

  public static Props props(long receiverId) {

    return Props.create(ReceiverActor.class, receiverId);
  }

  @Override
  public Receive createReceive() {

    return receiveBuilder()
        .match(Message.class, message -> {
          log.info("Receiver #{} received message {} from sender {}",
              receiverId, message.getMessageId(), message.getSenderId());
        }).matchAny(object -> {
          log.warning("Receiver #{} ignoring unexpected message of class {}",
              receiverId, object.getClass());
        }).build();
  }
}
