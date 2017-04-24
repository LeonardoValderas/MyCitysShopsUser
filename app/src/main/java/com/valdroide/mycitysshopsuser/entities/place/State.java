package com.valdroide.mycitysshopsuser.entities.place;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;


@Table(database = ShopsDatabase.class)
public class State extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_STATE_KEY;

    @Column
    @SerializedName("id_country")
    public int ID_COUNTRY_FOREIGN;

    @Column
    @SerializedName("is_active")
    public int IS_ACTIVE;

    @Column
    @SerializedName("state")
    public String STATE;

    public State() {
    }

    public int getID_STATE_KEY() {
        return ID_STATE_KEY;
    }

    public void setID_STATE_KEY(int ID_STATE_KEY) {
        this.ID_STATE_KEY = ID_STATE_KEY;
    }

    public int getID_COUNTRY_FOREIGN() {
        return ID_COUNTRY_FOREIGN;
    }

    public void setID_COUNTRY_FOREIGN(int ID_COUNTRY_FOREIGN) {
        this.ID_COUNTRY_FOREIGN = ID_COUNTRY_FOREIGN;
    }

    public int getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(int IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public String getSTATE() {
        return STATE;
    }

    public String toString() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }
}
