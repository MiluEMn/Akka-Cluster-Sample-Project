package com.emnify.acsp.sender;

import com.emnify.acsp.common.AbstractAcspTest;
import com.emnify.acsp.sender.helper.RemoteAppHelper;

import akka.actor.Props;
import akka.testkit.EventFilter;
import org.junit.Ignore;
import org.junit.Test;

public class ClusterListenerTest extends AbstractAcspTest {

  @Ignore
  @Test
  public void testAddRemoveMember() {

    system.actorOf(Props.create(ClusterListener.class), "clusterListener");
    final RemoteAppHelper helper = new RemoteAppHelper("test-remote-application.conf");

    EventFilter.info(null, "akka://acsp-test/user/clusterListener",
        null, "Member is up*", 1).intercept(() -> {
      helper.startUp();
      return null;
    }, system);

    EventFilter.info(null, "akka://acsp-test/user/clusterListener",
        null, "Member is removed*", 1).intercept(() -> {
      helper.leaveCluster();
      return null;
    }, system);

    helper.shutdown();
  }
}
