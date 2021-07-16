package com.company.server;

import com.company.AppGlobals;
import com.company.BadParametersException;
import com.company.Message;
import com.company.SessionState;
import com.company.dbstore.*;
import net.bytebuddy.implementation.bytecode.Throw;
import org.eclipse.jetty.server.Server;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static java.lang.Thread.sleep;

class TestServlets extends Mockito {
    private static final int testPort = 5433;
    private static final String serverUrl = "http://localhost:5433/";
    private static final String waitForMessageServletAddress = "waitForMessage?";
    private static final String sendMessageServletAddress = "sendMessage?";
    private static final String endSessionServletAddress = "endSession?";
    private static final String createSessionServletAddress = "createSession?";
    private static final String userLoginServletAddress = "userLogin?";
    //private static Server server;
    private static AppGlobals appGlobals;

    @BeforeAll
    static void startServer() throws Exception {
        LoginStoreImpl loginStore = mock(LoginStoreImpl.class);
        CustomerStoreImpl customerStore = mock(CustomerStoreImpl.class);
        SessionStoreImpl sessionStore = mock(SessionStoreImpl.class);
        MessageStoreImpl messageStore = mock(MessageStoreImpl.class);
        RepresentativeStoreImpl representativeStore = mock(RepresentativeStoreImpl.class);
        PubSubStoreImpl pubSubStore = mock(PubSubStoreImpl.class);
        RoomStoreImpl roomStore = mock(RoomStoreImpl.class);
        appGlobals = new AppGlobals(customerStore, messageStore, representativeStore, sessionStore, pubSubStore, loginStore, roomStore);
        Server server = new SetupServer(testPort).setupServer();
        server.start();
        prepareMock();
    }

    private static void prepareMock() {
        when(appGlobals.getSessionStore().validateSessionExistence("session9999")).thenReturn(SessionState.NOT_EXISTS);
        when(appGlobals.getSessionStore().validateSessionExistence("session5555")).thenReturn(SessionState.EXISTS_AND_NOT_ENDED);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) {
                ArrayList<Message> arr = new ArrayList<>();
                arr.add(new Message("test message1", false, 2));
                arr.add(new Message("test message2", false, 3));
                return arr;
            }
        }).when(appGlobals.getMessageStore()).retrieveAllMessagesSince("session5555", 1);
        doAnswer((new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) {
                appGlobals.userPubSub.put("session9876", 2);
                return SessionState.EXISTS_AND_NOT_ENDED;
            }
        })).when(appGlobals.getSessionStore()).validateSessionExistence("session9876");

        doAnswer((new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ArrayList<Message> arr = new ArrayList<>();
                arr.add(new Message("test message1", false, 2));
                return arr;
            }
        })).when(appGlobals.getMessageStore()).retrieveAllMessagesSince("session9876", 2);
        appGlobals.userPubSub.put("session9876", 0);
        doNothing().when(appGlobals.getMessageStore()).insertMessage("session4444", "send this message", false, 4);
        doNothing().when(appGlobals.getMessageStore()).insertMessage("session5555", "send this message", true, 3);
        appGlobals.repPubSub.put("session4444", 4);
        appGlobals.userPubSub.put("session4444", 4);
        appGlobals.repPubSub.put("session5555", 3);
        appGlobals.userPubSub.put("session5555", 3);
        doNothing().when(appGlobals.getSessionStore()).endSession("session1111");
        doNothing().when(appGlobals.getSessionStore()).endSession("session3333");
        doThrow(new RuntimeException("cannot end nonexistent session")).when(appGlobals.getSessionStore()).endSession("session2222");
        when(appGlobals.getSessionStore().insertSession("customer1111", "amit@trugman.com")).thenReturn("session1111");
        when(appGlobals.getSessionStore().insertSession("customer3333", "amit2@trugman.com")).thenReturn("session3333");
        when(appGlobals.getSessionStore().insertSession("customer2222", "amit@trugman.com")).thenThrow(new RuntimeException("randomized session already exists")); //randomized SID  does  exist in db
        when(appGlobals.getLoginStore().userLogin("testLogin@gmail.com", "testPassword")).thenReturn(true);
    }

    @Test
    void testUserLoginServlet() {
        String email = "testLogin@gmail.com";
        String password = "testPassword";
        Assertions.assertDoesNotThrow(() -> sendLoginRequest(userLoginServletAddress, password, email));
        Assertions.assertThrows(IOException.class, () -> sendLoginRequest(userLoginServletAddress, "abc", "ddd")); //invalid email format
    }

    @Test
    void testWaitForMessageServletBadFormat() {
        String badParams1 = "time=15:46&isRep=false";
        String badParams2 = "sid=session4321&isRep=true";
        String badParams3 = "sid=session6789&time=15:46";
        Assertions.assertThrows(Exception.class, () -> sendRequest(waitForMessageServletAddress + badParams1, "GET", null, false));
        Assertions.assertThrows(Exception.class, () -> sendRequest(waitForMessageServletAddress + badParams2, "GET", null, false));
        Assertions.assertThrows(Exception.class, () -> sendRequest(waitForMessageServletAddress + badParams3, "GET", null, false));
    }

    //@Test
    @RepeatedTest(30)
    void testWaitForMessageServletGoodRequest() {
        String goodParams1 = "sid=session5555&time=15:46&isRep=false";
        Assertions.assertDoesNotThrow(() -> sendRequest(waitForMessageServletAddress + goodParams1, "GET", null, false));
        String goodParams2 = "sid=session9876&time=6:00&isRep=true&syncpoint=2";
        Assertions.assertDoesNotThrow(() -> sendRequest(waitForMessageServletAddress + goodParams2, "GET", null, true));
    }

    @Test
    void testWaitForMessageServletBadRequest() {
        String params = "sid=session9999&time=15:46&isRep=false";
        Assertions.assertThrows(Exception.class, () -> sendRequest(waitForMessageServletAddress + params, "GET", null, false));
    }

    @Test
    void testSendMessageServletBadFormat() {
        String badParams1 = "sid=session2222&isRep=false";
        String badParams2 = "sid=session2222&messageNum=2";
        String badParams3 = "isRep=true&messageNum=3";
        String msg = "send this message";
        Assertions.assertThrows(Exception.class, () -> sendRequest(sendMessageServletAddress + badParams1, "POST", msg, false));
        Assertions.assertThrows(Exception.class, () -> sendRequest(sendMessageServletAddress + badParams2, "POST", msg, false));
        Assertions.assertThrows(Exception.class, () -> sendRequest(sendMessageServletAddress + badParams3, "POST", msg, false));
    }

    @Test
    void testSendMessageServletGoodRequest() {
        String msg = "send this message";
        String goodParams1 = "sid=session4444&isRep=true&messageNum=10";
        String goodParams2 = "sid=session4444&isRep=true&messageNum=1";
        String goodParams3 = "sid=session4444&isRep=true&messageNum=11";
        int userSize = appGlobals.userPubSub.size();
        int repSize = appGlobals.repPubSub.size();
        Assertions.assertEquals(userSize, appGlobals.userPubSub.size());
        Assertions.assertEquals(repSize, appGlobals.repPubSub.size());
        Assertions.assertDoesNotThrow(() -> sendRequest(sendMessageServletAddress + goodParams1, "POST", msg, false));
        Assertions.assertDoesNotThrow(() -> sendRequest(sendMessageServletAddress + goodParams2, "POST", msg, false));
        Assertions.assertEquals(userSize, appGlobals.userPubSub.size()); //pubsubs increase in size only when a new session is created... messages dont cause a new node to be created
        Assertions.assertEquals(repSize, appGlobals.repPubSub.size());


    }

    @Test
    void testCreateSessionServletBadFormat() {
        String badParams1 = "email=amit@trugman.com";
        String badParams2 = "cid=customer1234";
        Assertions.assertThrows(Exception.class, () -> sendRequest(createSessionServletAddress + badParams1, "GET", null, false));
        Assertions.assertThrows(Exception.class, () -> sendRequest(createSessionServletAddress + badParams2, "GET", null, false));
    }

    @Test
    void testEndSessionServletBadFormat() {
        String badParams1 = "&isRep=false&messageNum=2";
        String badParams2 = "sid=session2222&messageNum=2";
        String badParams3 = "sid=session2222&isRep=false";
        Assertions.assertThrows(Exception.class, () -> sendRequest(endSessionServletAddress + badParams1, "GET", null, false));
        Assertions.assertThrows(Exception.class, () -> sendRequest(endSessionServletAddress + badParams2, "GET", null, false));
        Assertions.assertThrows(Exception.class, () -> sendRequest(endSessionServletAddress + badParams3, "GET", null, false));
    }

    @Test
    void testCreateSessionServletGoodRequest() {
        int userSize = appGlobals.userPubSub.size();
        int repSize = appGlobals.repPubSub.size();
        String expectedResponse = "session1111";
        String goodParams = "cid=customer1111&email=amit@trugman.com";
        Assertions.assertDoesNotThrow(() -> sendGoodCreateSessionNotification(createSessionServletAddress + goodParams, expectedResponse));
        Assertions.assertEquals(userSize + 1, appGlobals.userPubSub.size());
        Assertions.assertEquals(repSize + 1, appGlobals.repPubSub.size());

        String expectedResponse2 = "session3333";
        String goodParams2 = "cid=customer3333&email=amit2@trugman.com";
        Assertions.assertDoesNotThrow(() -> sendGoodCreateSessionNotification(createSessionServletAddress + goodParams2, expectedResponse2));
        Assertions.assertEquals(userSize + 2, appGlobals.userPubSub.size());
        Assertions.assertEquals(repSize + 2, appGlobals.repPubSub.size());
    }

    @Test
    void testEndSessionServletGoodRequest() {
        int userSize = appGlobals.userPubSub.size();
        int repSize = appGlobals.repPubSub.size();
        String goodParams1 = "sid=session1111&isRep=false&messageNum=2";
        Assertions.assertDoesNotThrow(() -> sendEndSessionNotification(endSessionServletAddress + goodParams1));
        Assertions.assertEquals(userSize - 1, appGlobals.userPubSub.size()); //successful deletion
        Assertions.assertEquals(repSize - 1, appGlobals.repPubSub.size()); //successful deletion

        String goodParams2 = "sid=session3333&isRep=true&messageNum=4";
        Assertions.assertDoesNotThrow(() -> sendEndSessionNotification(endSessionServletAddress + goodParams2));
        Assertions.assertEquals(userSize - 2, appGlobals.userPubSub.size()); //successful deletion
        Assertions.assertEquals(repSize - 2, appGlobals.repPubSub.size());//successful deletion
    }

    @Test
    void testCreateSessionServletBadRequest() {
        String badParams = "cid=customer2222&email=amit@trugman.com";
        Assertions.assertThrows(Exception.class, () -> sendBadCreateSessionNotification(createSessionServletAddress + badParams));
    }

    @Test
    void testEndSessionServletBadRequest() {
        String badParams = "sid=session2222&isRep=false&messageNum=2";
        Assertions.assertThrows(Exception.class, () -> sendEndSessionNotification(endSessionServletAddress + badParams));
    }

    void sendEndSessionNotification(String endpoint) throws IOException {
        sendRequest(endpoint, "GET", null, false);
    }

    void sendGoodCreateSessionNotification(String endpoint, String expectedSession) throws IOException {
        JSONObject response = sendRequest(endpoint, "GET", null, false);
        JSONObject expectedResponse = new JSONObject().put("sessionId", expectedSession);
        if (!response.similar(expectedResponse)) {
            throw new RuntimeException("unexpected response string");
        }
    }

    void sendBadCreateSessionNotification(String endpoint) throws IOException {
        sendRequest(endpoint, "GET", null, false);
    }


    JSONObject sendLoginRequest(String endpoint, String password, String email) throws IOException {
        try {
            URL url = new URL(serverUrl + endpoint);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST");

            JSONObject ob = new JSONObject();
            ob.put("email", email);
            ob.put("password", password);

            byte[] out = ob.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;
            http.setFixedLengthStreamingMode(length);
            http.setDoOutput(true);
            http.connect();
            try (OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            StringBuilder resp = new StringBuilder();
            if (http.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream errorResponse = http.getErrorStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(errorResponse));
                while (reader.ready()) {
                    resp.append(reader.readLine());
                }
                throw new IOException(resp.toString());
            } else {
                InputStream response = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                while (reader.ready()) {
                    resp.append(reader.readLine());
                }
                return new JSONObject(resp.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw e;
        }
    }

    JSONObject sendRequest(String endpoint, String requestMethod, String payload, boolean wakeUp) throws IOException {
        try {
            URL url = new URL(serverUrl + endpoint);

            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod(requestMethod);

            if (requestMethod.equals("POST")) {
                http.setDoOutput(true);
                Map<String, String> arguments = new HashMap<>();
                arguments.put("message", payload);
                StringJoiner sj = new StringJoiner("&");
                for (Map.Entry<String, String> entry : arguments.entrySet()) {
                    //sj.add(entry.getKey() + " = " + entry.getValue());
                    sj.add(entry.getValue());

                }
                byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
                int length = out.length;
                http.setFixedLengthStreamingMode(length);
                //http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                http.connect();
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }
            }

            if (wakeUp) {
                new Thread(() -> {
                    try {
                        while (appGlobals.userPubSub.getValueByKey("session9876").getNumOfListeners() == 0) {
                            sleep(5);
                        }
                        appGlobals.userPubSub.put("session9876", 3); // trigger wakeup
                    } catch (InterruptedException e) {
                        throw new RuntimeException("wakeup thread error");
                    }
                }).start();
            }

            InputStream response = con.getInputStream();
            StringBuilder resp = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response));
            while (reader.ready()) {
                resp.append(reader.readLine());
            }
            return new JSONObject(resp.toString());
        } catch (IOException e) {
            //log here
            e.printStackTrace();
            throw e;
        }
    }

}
