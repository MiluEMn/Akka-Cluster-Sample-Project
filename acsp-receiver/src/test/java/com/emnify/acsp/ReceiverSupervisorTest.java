package com.emnify.acsp;

import com.emnify.acsp.common.AbstractAcspTest;
import com.emnify.acsp.common.Message;
import com.emnify.acsp.helper.CheckActorExistsMessage;
import com.emnify.acsp.helper.IdentifyHelperActor;
import com.emnify.acsp.receiver.ReceiverSupervisor;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.testkit.EventFilter;
import akka.testkit.TestKit;
import akka.util.Timeout;
import org.junit.Assert;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ReceiverSupervisorTest extends AbstractAcspTest {

  @Test
  public void testCreateReceiverActors() throws Exception {

    system.actorOf(Props.create(ReceiverSupervisor.class), "rsv");
    ActorSelection rc1 = system.actorSelection("/user/rsv/receiver1");
    ActorSelection rc2 = system.actorSelection("/user/rsv/receiver2");
    ActorSelection rc3 = system.actorSelection("/user/rsv/receiver3");

    TestKit testKit = new TestKit(system);
    ActorRef iha = system.actorOf(Props.create(IdentifyHelperActor.class), "iha");

    iha.tell(new CheckActorExistsMessage(1, rc1), testKit.testActor());
    Optional<ActorRef> res1 = testKit.expectMsgClass(Optional.class);
    assertSameActor(rc1, res1);

    iha.tell(new CheckActorExistsMessage(2, rc2), testKit.testActor());
    Optional<ActorRef> res2 = testKit.expectMsgClass(Optional.class);
    assertSameActor(rc2, res2);

    iha.tell(new CheckActorExistsMessage(3, rc3), testKit.testActor());
    Assert.assertFalse(testKit.expectMsgClass(Optional.class).isPresent());
  }

  @Test
  public void testMessageDistribution() {

    ActorRef rsv = system.actorOf(Props.create(ReceiverSupervisor.class), "rsv");

    EventFilter.info(null, "akka://acps-test/user/rsv/receiver1", null,
        "Receiver #1 received message*", 2).intercept(() -> {
      rsv.tell(new Message(1L, 1L), ActorRef.noSender());
      rsv.tell(new Message(2L, 1L), ActorRef.noSender());
      rsv.tell(new Message(2L, 1L), ActorRef.noSender());
      rsv.tell(new Message(3L, 1L), ActorRef.noSender());
      rsv.tell(new Message(3L, 1L), ActorRef.noSender());
      return null;
    }, system);

    EventFilter.info(null, "akka://acps-test/user/rsv/receiver2", null,
        "Receiver #2 received message*", 3).intercept(() -> {
      rsv.tell(new Message(1L, 1L), ActorRef.noSender());
      rsv.tell(new Message(2L, 1L), ActorRef.noSender());
      rsv.tell(new Message(2L, 1L), ActorRef.noSender());
      rsv.tell(new Message(3L, 1L), ActorRef.noSender());
      rsv.tell(new Message(3L, 1L), ActorRef.noSender());
      return null;
    }, system);
  }

  private void assertSameActor(ActorSelection expected, Optional<ActorRef> actual)
      throws Exception {

    Assert.assertTrue(actual.isPresent());
    ActorRef expectedRef = Await.result(expected.resolveOne(new Timeout(5L, TimeUnit.SECONDS)),
        Duration.create(5L, TimeUnit.SECONDS));
    Assert.assertEquals(expectedRef, actual.get());
  }
}
