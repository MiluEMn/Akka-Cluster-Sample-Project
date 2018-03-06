package com.emnify.acsp.sender;

import com.emnify.acsp.common.AbstractAcspTest;
import com.emnify.acsp.common.Message;

import akka.actor.ActorRef;
import akka.testkit.TestKit;
import org.junit.Assert;
import org.junit.Test;

public class SenderTest extends AbstractAcspTest {

  @Test
  public void testSendMessage() {

    TestKit testKit = new TestKit(system);
    ActorRef sender = system.actorOf(SenderActor.props(1L, testKit.testActor()));

    sender.tell("Do something", ActorRef.noSender());
    Message message = testKit.expectMsgClass(Message.class);
    Assert.assertEquals(1L, message.getMessageId());
    Assert.assertEquals(1L, message.getSenderId());

    sender.tell("Do something again", ActorRef.noSender());
    message = testKit.expectMsgClass(Message.class);
    Assert.assertEquals(2L, message.getMessageId());
    Assert.assertEquals(1L, message.getSenderId());
  }
}
