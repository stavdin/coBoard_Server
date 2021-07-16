package com.company.server;

import com.company.AppGlobals;
import com.company.dbstore.*;
import org.eclipse.jetty.server.Server;

public class WhiteboardServer {


    public WhiteboardServer() {

    }
    // RepresentativeStoreImpl.getInstance(); this is the instance of the singleton class RepresentativeStoreImpl
    // CustomerStoreImpl.getInstance(); this is the instance of the singleton class CustomerStoreImpl

    public static void main(String[] args) throws Exception {

        CustomerStoreImpl customerStoreImpl = new CustomerStoreImpl();
        SessionStoreImpl sessionStoreImpl = new SessionStoreImpl();
        MessageStoreImpl messageStoreImpl = new MessageStoreImpl();
        PubSubStoreImpl pubSubStoreImpl = new PubSubStoreImpl();
        RepresentativeStoreImpl representativeStoreImpl = new RepresentativeStoreImpl();
        LoginStoreImpl loginStoreImpl = new LoginStoreImpl();
        RoomStoreImpl roomStoreImpl = new RoomStoreImpl();
        AppGlobals appGlobals = new AppGlobals(customerStoreImpl, messageStoreImpl, representativeStoreImpl, sessionStoreImpl, pubSubStoreImpl, loginStoreImpl, roomStoreImpl);
//
//        appGlobals.getPubSubStore().fetchPubSubFromDatabase(appGlobals.userPubSub); //initialize pubSub with session data
//        appGlobals.getPubSubStore().fetchPubSubFromDatabase(appGlobals.repPubSub); //initialize pubSub with session data
        Server server = new SetupServer(8081).setupServer();
        server.start();
    }


}
