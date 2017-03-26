package com.valdroide.mycitysshopsuser.entities.place;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class Country extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_COUNTRY_KEY;

    @Column
    @SerializedName("country")
    public String COUNTRY;

    public Country() {
    }

    public void setID_COUNTRY_KEY(int ID_COUNTRY_KEY) {
        this.ID_COUNTRY_KEY = ID_COUNTRY_KEY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public String toString() {
        return COUNTRY;
    }

    public int getID_COUNTRY_KEY() {
        return ID_COUNTRY_KEY;
    }
}
