package com.company.dbstore;

import com.company.Message;
import com.company.pubsub.PubSubStruct;

public interface PubSubStore{
    public void fetchPubSubFromDatabase(PubSubStruct<String, Integer> pubSub);
}
