package com.emnify.acsp.sender.helper;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.cluster.Cluster;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class RemoteAppHelper {

  private Config config;
  private ActorSystem system;
  private LoggingAdapter log;

  public RemoteAppHelper(String configFile) {

    config = ConfigFactory.load(configFile);
  }

  public void startUp() {

    system = ActorSystem.create("acsp-test", config);
    log = Logging.getLogger(system, this);
  }

  public void leaveCluster() {

    log.warning("Remote App leave cluster");
    Cluster cluster = Cluster.get(system);
    cluster.leave(cluster.selfAddress());
  }

  public void shutdown() {

    log.warning("Remote App shutdown");
    system.terminate();
  }
}
