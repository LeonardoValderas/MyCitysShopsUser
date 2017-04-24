package com.valdroide.mycitysshopsuser.entities.place;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;


@Table(database = ShopsDatabase.class)
public class DatePlace extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_PLACE_KEY;

    @Column
    @SerializedName("country_date")
    public String COUNTRY_DATE;

    @Column
    @SerializedName("state_date")
    public String STATE_DATE;

    @Column
    @SerializedName("city_date")
    public String CITY_DATE;

    @Column
    @SerializedName("table_date")
    public String TABLE_DATE;

    public DatePlace() {
    }

    public int getID_PLACE_KEY() {
        return ID_PLACE_KEY;
    }

    public void setID_PLACE_KEY(int ID_PLACE_KEY) {
        this.ID_PLACE_KEY = ID_PLACE_KEY;
    }

    public String getCOUNTRY_DATE() {
        return COUNTRY_DATE;
    }

    public void setCOUNTRY_DATE(String COUNTRY_DATE) {
        this.COUNTRY_DATE = COUNTRY_DATE;
    }

    public String getSTATE_DATE() {
        return STATE_DATE;
    }

    public void setSTATE_DATE(String STATE_DATE) {
        this.STATE_DATE = STATE_DATE;
    }

    public String getCITY_DATE() {
        return CITY_DATE;
    }

    public void setCITY_DATE(String CITY_DATE) {
        this.CITY_DATE = CITY_DATE;
    }

    public String getTABLE_DATE() {
        return TABLE_DATE;
    }

    public void setTABLE_DATE(String TABLE_DATE) {
        this.TABLE_DATE = TABLE_DATE;
    }
}
