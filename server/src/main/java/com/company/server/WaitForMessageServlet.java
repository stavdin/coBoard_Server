package com.company.server;

import com.company.AppGlobals;
import com.company.Message;
import com.company.SessionState;
import com.company.VariableFormat;
import com.company.pubsub.PubSubStruct;
import com.company.pubsub.UnsubscribeObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.company.AppGlobals.getInstance;

public class WaitForMessageServlet extends HttpServlet {
    private static VariableFormat format = VariableFormat.getInstance();
    private final Object ob = new Object();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //this is a request
        AppGlobals appGlobals = getInstance();
        String sid = req.getParameter("sid");
        String time = req.getParameter("time");
        String isRepStr = req.getParameter("isRep");
        String syncPointStr = req.getParameter("syncpoint"); //null means no the last message the client received doesnt exist - first waitformessage
        if (!format.isRepFormatOk(isRepStr) || !format.isSessionIdFormatOk(sid) || !format.isTimeFormatOk(time) || !format.isSyncpointOk(syncPointStr)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid sessionId, message, isRep, or syncpoint format");
            return;
        }
        var syncpoint = (syncPointStr == null) ? 0 : appGlobals.decode(syncPointStr);
        boolean isRep = Boolean.parseBoolean(isRepStr);
        var sessionState = appGlobals.getSessionStore().validateSessionExistence(sid);

        if (sessionState == SessionState.EXISTS_AND_NOT_ENDED) {
            // long polling code - race

            if (isRep) {
                handleLongPolling(appGlobals, syncpoint, sid, resp, appGlobals.userPubSub);
            } else {
                handleLongPolling(appGlobals, syncpoint, sid, resp, appGlobals.repPubSub);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "cannot wait for message on a nonexistent or over session");
        }

    }


    private void handleLongPolling(AppGlobals appGlobals, Integer syncpoint, String sid, HttpServletResponse resp, PubSubStruct<String, Integer> pubSub) throws IOException {
        Integer pubSubSyncpoint = (pubSub.getValueByKey(sid).getValue() != null) ? pubSub.getValueByKey(sid).getValue() : 0;
        if (syncpoint.compareTo(pubSubSyncpoint) < 0) {
            respondWithEverything(sid, resp, syncpoint);
        } else if (syncpoint.compareTo(pubSubSyncpoint) == 0) {
            // here we sub and check again. if yes we respond and unsub. if no we wait.
            final Object syncObject = new Object();

            UnsubscribeObject unsubscribeObject = pubSub.subscribe(sid, (newValue) -> {
                synchronized (syncObject) {
                    syncObject.notify();
                }
            });

            try {
                synchronized (syncObject) {
                    pubSubSyncpoint = (pubSub.getValueByKey(sid).getValue() != null) ? pubSub.getValueByKey(sid).getValue() : 0;
                    while (syncpoint.compareTo(pubSubSyncpoint) == 0) { // as long as there are no messages to retrieve
                        syncObject.wait();
                        pubSubSyncpoint = (pubSub.getValueByKey(sid).getValue() != null) ? pubSub.getValueByKey(sid).getValue() : 0;
                    }
                }

                if (syncpoint.compareTo(pubSubSyncpoint) < 0) {
                    respondWithEverything(sid, resp, syncpoint);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "thread interrupted");
            } finally {
                unsubscribeObject.unsubscribe();
            }
        } else {
            //error, we god a syncpoint that is larger than the one stored in pubsub
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid syncpoint.");
        }
    }

    private void respondWithEverything(String sid, HttpServletResponse resp, Integer sync) throws IOException {
        ArrayList<Message> messages = AppGlobals.getInstance().getMessageStore().retrieveAllMessagesSince(sid, sync);
        var newSyncpoint3 = findNewSyncpoint(messages);
        //var newSyncpoint2 = messages.stream().mapToInt((msg) -> msg.getMsgNum()).max().getAsInt();
        //var sum = messages.stream().mapToInt((msg) -> msg.getMsgNum()).reduce(0, (identity, element) -> identity + element);
        //var newSyncpoint = messages.stream().max((a, b) -> a.getMsgNum().compareTo(b.getMsgNum())).get().getMsgNum();
        var json = new JSONObject();
        json.put("syncPoint", newSyncpoint3);
        json.put("messages", new JSONArray(messages.stream().map((msg) -> msg.getText()).collect(Collectors.toList())));
        resp.getOutputStream().println(json.toString());
    }

    private int findNewSyncpoint(ArrayList<Message> arr) {
        int newSyncpoint = -1;
        int temp;
        int i = 0;
        while (i < arr.size()) {
            temp = arr.get(i).getMsgNum();
            if (temp > newSyncpoint) {
                newSyncpoint = temp;
            }
            ++i;
        }
        return newSyncpoint;
    }
}
