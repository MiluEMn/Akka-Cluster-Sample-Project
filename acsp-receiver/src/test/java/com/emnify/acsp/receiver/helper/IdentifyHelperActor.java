package com.emnify.acsp.receiver.helper;

import akka.actor.AbstractActor;
import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.Identify;

import java.util.Optional;

public class IdentifyHelperActor extends AbstractActor {

  private ActorRef originalSender;

  @Override
  public Receive createReceive() {

    return receiveBuilder()
        .match(CheckActorExistsMessage.class, message -> {
          if (null == originalSender) {
            originalSender = getSender();
          }
          message.getActorToCheck().tell(new Identify(message.getId()), getSelf());
        })
        .match(ActorIdentity.class, id -> {
          if (id.getActorRef().isPresent()) {
            originalSender.tell(Optional.of(id.getActorRef().get()), getSelf());
          } else {
            originalSender.tell(Optional.empty(), getSelf());
          }
        })
        .build();
  }
}
