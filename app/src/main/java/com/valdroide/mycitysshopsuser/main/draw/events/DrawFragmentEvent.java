package com.valdroide.mycitysshopsuser.main.draw.events;

import com.valdroide.mycitysshopsuser.entities.shop.Draw;

import java.util.List;

public class DrawFragmentEvent {
    private int type;
    private String error;
    private List<Draw> drawList;
    public static final int DRAWS = 0;
    public static final int PARTICIPATESUCCESS = 1;
    public static final int ERROR = 2;
    public static final int WITHOUTCHANGE = 3;
    public static final int GETDRAWSREFRESH = 4;

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

    public List<Draw> getDrawList() {
        return drawList;
    }

    public void setDrawList(List<Draw> drawList) {
        this.drawList = drawList;
    }
}
