package com.emnify.acsp.receiver;

import com.emnify.acsp.common.Message;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.List;

public class ReceiverSupervisor extends AbstractActor {

  private List<ActorRef> receiverActors = new ArrayList<>();

  @Override
  public void preStart() {

    Long numReceivers = getContext().system().settings().config().getLong("num-receivers");
    for(long l = 1L; l <= numReceivers; l++) {
      receiverActors.add(getContext().actorOf(ReceiverActor.props(l), "receiver" + l));
    }
  }

  @Override
  public Receive createReceive() {

    return receiveBuilder()
        .match(Message.class,
            message -> getReceiver(message.getMessageId()).tell(message, getSender()))
        .build();
  }

  private ActorRef getReceiver(long messageId) {

    return receiverActors.get(((int) messageId % receiverActors.size()));
  }
}
