package com.valdroide.mycitysshopsuser.main.legal.events;


public class LegalActivityEvent {
    public static final int DATASUCCESS = 0;
    public static final int ERRRO = 1;
    private int type;
    private String about;
    private String legal;
    private String error;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
