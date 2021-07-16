package com.company.pubsub;

import com.company.pubsub.PubSubException;
import com.company.pubsub.PubSubStruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PubSubStructTest {
    private PubSubStruct<Integer,String> pubSub = new PubSubStruct<Integer,String>();

    @Test
    void testPubSubStructPutAndRemove() throws PubSubException {
        String str = " test string ";
        Integer key = 10;
        Assertions.assertEquals(pubSub.size(),0);
        pubSub.put(key,str);
        Assertions.assertEquals(pubSub.size(),1);
        Assertions.assertDoesNotThrow(()->pubSub.remove(key));
        Assertions.assertEquals(pubSub.size(),0);
        //Assertions.assertThrows(PubSubException.class, ()-> pubSub.remove(12), "PubSubException expected here" ); //test remove non existent key
    }
}
