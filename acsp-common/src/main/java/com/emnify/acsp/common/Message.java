package com.emnify.acsp.common;

import java.io.Serializable;

public class Message implements Serializable {

  private long messageId;
  private long senderId;

  public Message(long messageId, long senderId) {

    this.messageId = messageId;
    this.senderId = senderId;
  }

  public long getMessageId() {
    return messageId;
  }

  public long getSenderId() {
    return senderId;
  }
}
