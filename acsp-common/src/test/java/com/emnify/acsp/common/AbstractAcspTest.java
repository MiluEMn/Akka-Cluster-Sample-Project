package com.emnify.acsp.common;

import static akka.testkit.TestKit.shutdownActorSystem;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import org.junit.After;
import org.junit.Before;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public abstract class AbstractAcspTest {

  protected ActorSystem system;

  @Before
  public void setupSystem() {

    Config config = ConfigFactory.load("test-application.conf");
    system = ActorSystem.create("acps-test", config);
  }

  @After
  public void shutdownSystem() {

    shutdownActorSystem(system, Duration.create(5, TimeUnit.SECONDS), true);
  }
}
