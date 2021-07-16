package com.company.pubsub;

import java.util.LinkedList;

public class PubSubNode<T> {
    private T value; // Message
    private LinkedList<ListenToNode<T>> subs = new LinkedList<>(); // list of subscribers

    public PubSubNode(T value) {
        this.value = value;
    }


    synchronized boolean unsubscribeListener(ListenToNode<T> itemToRemove) {
        if (subs.contains(itemToRemove)) {
            subs.remove(itemToRemove);
            return true;
        }
        return false;
    }

    public T getValue() {
        return value;
    }

    public synchronized void setValue(T newValue) {
        this.value = newValue;
        for (ListenToNode<T> node : subs) {
            node.onChange(newValue);
        }
    }

    public synchronized UnsubscribeObject<T> subscribe(ListenToNode<T> listener) {
        this.subs.add(listener);
        return new UnsubscribeObject<T>(this, listener);
    }

    public synchronized int getNumOfListeners(){
        return subs.size();
    }

}
