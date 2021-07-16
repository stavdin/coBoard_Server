package com.company;

public enum SessionState {
    NOT_EXISTS (0),
    EXISTS_AND_ENDED (1),
    EXISTS_AND_NOT_ENDED (2);

    private final int val;
    private SessionState(int val){
        this.val = val;
    }
    public int getVal(){
        return val;
    }
}
