package com.company.pubsub;

public interface ListenToNode <T> {
    public void onChange(T newValue);

}
