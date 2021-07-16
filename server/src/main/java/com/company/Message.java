package com.company;

public class Message {
    private String text;
    private boolean isTerminationMessage;
    private Integer msgNum;

    public Message(String text, boolean over, Integer msgNum) {
        this.text = text;
        this.isTerminationMessage = over;
        this.msgNum = msgNum;
    }

    public String getText() {
        return text;
    }

    public Integer getMsgNum() {
        return msgNum;
    }
}
