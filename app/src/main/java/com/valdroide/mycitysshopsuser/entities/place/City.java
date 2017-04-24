package com.valdroide.mycitysshopsuser.entities.place;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;


@Table(database = ShopsDatabase.class)
public class City extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_CITY_KEY;

    @Column
    @SerializedName("id_state")
    public int ID_STATE_FOREIGN;

    @Column
    @SerializedName("is_active")
    public int IS_ACTIVE;

    @Column
    @SerializedName("city")
    public String CITY;

    public City() {
    }

    public int getID_CITY_KEY() {
        return ID_CITY_KEY;
    }

    public void setID_CITY_KEY(int ID_CITY_KEY) {
        this.ID_CITY_KEY = ID_CITY_KEY;
    }

    public int getID_STATE_FOREIGN() {
        return ID_STATE_FOREIGN;
    }

    public void setID_STATE_FOREIGN(int ID_STATE_FOREIGN) {
        this.ID_STATE_FOREIGN = ID_STATE_FOREIGN;
    }

    public int getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(int IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public String getCITY() {
        return CITY;
    }

    public String toString() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }
}
