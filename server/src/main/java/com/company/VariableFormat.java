package com.company;

public class VariableFormat {
    private static VariableFormat instance = new VariableFormat();

    private VariableFormat() {
        instance = getInstance();
    }

    public static VariableFormat getInstance() {
        return instance;
    }

    public boolean isSessionIdFormatOk(String sid) {
        if (sid == null) {
            return false;
        }
        return true;
    }

    public boolean isCustomerIdFormatOk(String cid) {
        if (cid == null) {
            return false;
        }
        return true;
    }

    public boolean isTimeFormatOk(String t) {
        if (t == null)
            return false;
//        try {
//            Time time = Time.valueOf(t);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
        return true;
    }

    public boolean isEmailFormatOk(String email) {
        if (email == null || email.length() > 20 || !email.contains("@")) {
            return false;
        }
        return true;
    }
    public boolean isPasswordFormatOk(String pass) {
        if (pass == null || pass.length() > 20) {
            return false;
        }
        return true;
    }

    public boolean isRepFormatOk(String isRepStr) {
        if (!isRepStr.toLowerCase().equals("true") && !isRepStr.toLowerCase().equals("false")) {
            return false;
        }
        return true;
    }

    public boolean isMessageNumOk(String messageNum) {
        try {
            int num = Integer.parseInt(messageNum);
            if (num <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMessageFormatOk(String text) {
        if (text == null || text.length() > 75) {
            return false;
        }
        return true;
    }

    public boolean isSyncpointOk(String syncpoint) {
        try {
            if (syncpoint == null) {
                return true;
            }
            AppGlobals.getInstance().decode(syncpoint);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
