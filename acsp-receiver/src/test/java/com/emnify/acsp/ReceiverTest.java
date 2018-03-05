package com.emnify.acsp;

import com.emnify.acsp.common.AbstractAcspTest;
import com.emnify.acsp.common.Message;
import com.emnify.acsp.receiver.ReceiverActor;

import akka.actor.ActorRef;
import akka.testkit.EventFilter;
import akka.testkit.TestKit;
import org.junit.Test;

public class ReceiverTest extends AbstractAcspTest {

  @Test
  public void testMessageReceive() {

    TestKit testKit = new TestKit(system);
    ActorRef receiver = system.actorOf(ReceiverActor.props(1L), "rec1");

    EventFilter.warning(null, receiver.path().toString(), null,
        "Receiver #1 ignoring unexpected message*", 1).intercept(() -> {
      receiver.tell("Hello", testKit.testActor());
      testKit.expectNoMsg();
      return null;
    }, system);

    EventFilter.info(null, receiver.path().toString(), null,
        "Receiver #1 received message*", 1).intercept(() -> {
      receiver.tell(new Message(1L, 1L), testKit.testActor());
      testKit.expectNoMsg();
      return null;
    }, system);
  }
}
