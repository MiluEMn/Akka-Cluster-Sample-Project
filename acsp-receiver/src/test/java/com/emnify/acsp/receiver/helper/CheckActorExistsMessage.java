package com.emnify.acsp.receiver.helper;

import akka.actor.ActorSelection;

public class CheckActorExistsMessage {

  private int id;
  private ActorSelection actorToCheck;

  public CheckActorExistsMessage(int id, ActorSelection actorToCheck) {

    this.id = id;
    this.actorToCheck = actorToCheck;
  }

  public int getId() {

    return id;
  }

  public ActorSelection getActorToCheck() {

    return actorToCheck;
  }
}
