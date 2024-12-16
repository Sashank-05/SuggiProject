package main.java.models;

import java.io.Serializable;

public class Message implements Serializable {
  private String type; // "ORDER", "ACCEPT", "ASSIGN", etc.
  private String content; // Content of the message (order details, status, etc.)

  public Message(String type, String content) {
    this.type = type;
    this.content = content;
  }

  public String getType() {
    return type;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "main.java.models.Message[type=" + type + ", content=" + content + "]";
  }
}
