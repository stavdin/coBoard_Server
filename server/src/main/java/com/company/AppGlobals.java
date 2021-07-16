package com.company;

import com.company.dbstore.*;
import com.company.pubsub.PubSubStruct;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AppGlobals {
    private static AppGlobals instance;
    public PubSubStruct<String, Integer> userPubSub;
    public PubSubStruct<String, Integer> repPubSub;
    private CustomerStore customerStore;
    private PubSubStore pubSubStore;
    private RepresentativeStore representativeStore;
    private SessionStore sessionStore;
    private MessageStore messageStore;
    private LoginStore loginStore;
    private RoomStore roomStore;
    public static final DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");


    public AppGlobals(CustomerStore c, MessageStore m, RepresentativeStore r, SessionStore s, PubSubStore p, LoginStore l, RoomStoreImpl roomStore) {
        customerStore = c;
        messageStore = m;
        representativeStore = r;
        pubSubStore = p;
        sessionStore = s;
        loginStore = l;
        this.roomStore = roomStore;
        instance = this;
        this.userPubSub = new PubSubStruct<String, Integer>();
        this.repPubSub = new PubSubStruct<String, Integer>();

    }

    public static AppGlobals getInstance() {
        return instance;
    }

    public CustomerStore getCustomerStore() {
        return customerStore;
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    public PubSubStore getPubSubStore() {
        return pubSubStore;
    }

    public RepresentativeStore getRepresentativeStore() {
        return representativeStore;
    }

    public SessionStore getSessionStore() {
        return sessionStore;
    }

    public LoginStore getLoginStore() {
        return loginStore;
    }

    public RoomStore getRoomStore() {
        return roomStore;
    }


    public String encode(int num) {
        return Integer.toString(num);
    }

    public int decode(String str) {
        return Integer.parseInt(str);
    }
}
