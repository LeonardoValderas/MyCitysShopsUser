package com.valdroide.mycitysshopsuser.main.support.events;

public class SupportActivityEvent {
    private int type;
    private String error;
    public static final int SENDEMAILSUCCESS = 0;
    public static final int ERROR = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
