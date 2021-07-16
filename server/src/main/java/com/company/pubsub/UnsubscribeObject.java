package com.company.pubsub;

public class UnsubscribeObject<T> {
    private PubSubNode<T> node; // the node we disconnect from. // already has list of subscribers in it
    private ListenToNode<T> objectToDisconnect; //the listener we disconnect

    UnsubscribeObject(PubSubNode<T> node, ListenToNode<T> t) {
        //t is the object we disconnect from the node provided
        this.node = node;
        objectToDisconnect = t;
    }

    public void unsubscribe() {
        if (!node.unsubscribeListener(objectToDisconnect)) {
            throw new RuntimeException("no item to disconnect from");
        }
    }

}
