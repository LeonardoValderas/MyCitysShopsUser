package com.valdroide.mycitysshopsuser.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class Support extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_SUPPORT_KEY;

    @Column
    @SerializedName("to_email")
    public String TO;

    @Column
    @SerializedName("from_email")
    public String FROM;

    @Column
    @SerializedName("pass")
    public String PASS;

    @Column
    @SerializedName("date_unique")
    public String DATE_UNIQUE;

    public Support() {
    }

    public int getID_SUPPORT_KEY() {
        return ID_SUPPORT_KEY;
    }

    public void setID_SUPPORT_KEY(int ID_SUPPORT_KEY) {
        this.ID_SUPPORT_KEY = ID_SUPPORT_KEY;
    }

    public String getTO() {
        return TO;
    }

    public void setTO(String TO) {
        this.TO = TO;
    }

    public String getFROM() {
        return FROM;
    }

    public void setFROM(String FROM) {
        this.FROM = FROM;
    }

    public String getPASS() {
        return PASS;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }

    public String getDATE_UNIQUE() {
        return DATE_UNIQUE;
    }

    public void setDATE_UNIQUE(String DATE_UNIQUE) {
        this.DATE_UNIQUE = DATE_UNIQUE;
    }
}
