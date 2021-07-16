package com.company.pubsub;

import com.company.pubsub.PubSubNode;
import com.company.pubsub.UnsubscribeObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class PubSubNodeTest {
    private String temp = "before change";
    private boolean onChangeCalled = false;

    @Test
    void testOnChange() {
        PubSubNode<String> node = new PubSubNode<>("before change");
        node.subscribe((String newValue)  -> {
                temp = node.getValue();
        System.out.println("on change called");
        });
//        node.subscribe(new ListenToNode<String>() {
//            @Override
//            public void onChange(String newValue) {
//                temp = node.getValue();
//                System.out.println("on change called");
//            }
//        });
        node.setValue("after change");
        Assertions.assertEquals(node.getValue(), "after change");
    }
    @Test
    void testUnsubscribe(){
        PubSubNode<String> node = new PubSubNode<>("node string");
        UnsubscribeObject<String> str = node.subscribe((String newValue)  -> {
            onChangeCalled = true;
        });
        str.unsubscribe();
        Assertions.assertFalse(onChangeCalled);

    }
}
