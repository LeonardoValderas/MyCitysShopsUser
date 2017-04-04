package com.valdroide.mycitysshopsuser.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class Token extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_TOKEN_KEY;

    @Column
    @SerializedName("token")
    public String TOKEN;

    @Column
    @SerializedName("id_city")
    public int ID_CITY_FOREIGN;


    public Token() {
    }

    public int getID_TOKEN_KEY() {
        return ID_TOKEN_KEY;
    }

    public void setID_TOKEN_KEY(int ID_TOKEN_KEY) {
        this.ID_TOKEN_KEY = ID_TOKEN_KEY;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }
}
