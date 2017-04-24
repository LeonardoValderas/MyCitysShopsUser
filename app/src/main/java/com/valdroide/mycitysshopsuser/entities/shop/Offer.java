package com.valdroide.mycitysshopsuser.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class Offer extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_OFFER_KEY;

    @Column
    @SerializedName("id_shop")
    public int ID_SHOP_FOREIGN;

    @Column
    @SerializedName("id_city")
    public int ID_CITY_FOREIGN;
    @Column
    @SerializedName("title")
    public String TITLE;

    @Column
    @SerializedName("offer")
    public String OFFER;
    @Column
    @SerializedName("url_image")
    public String URL_IMAGE;

    @Column
    @SerializedName("is_active")
    public int IS_ACTIVE;
    @Column
    @SerializedName("date_unique")
    public String DATE_UNIQUE;

    public Offer() {
    }

    public int getID_OFFER_KEY() {
        return ID_OFFER_KEY;
    }

    public void setID_OFFER_KEY(int ID_OFFER_KEY) {
        this.ID_OFFER_KEY = ID_OFFER_KEY;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getOFFER() {
        return OFFER;
    }

    public void setOFFER(String OFFER) {
        this.OFFER = OFFER;
    }

    public int getID_SHOP_FOREIGN() {
        return ID_SHOP_FOREIGN;
    }

    public void setID_SHOP_FOREIGN(int ID_SHOP_FOREIGN) {
        this.ID_SHOP_FOREIGN = ID_SHOP_FOREIGN;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }

    public String getURL_IMAGE() {
        return URL_IMAGE;
    }

    public void setURL_IMAGE(String URL_IMAGE) {
        this.URL_IMAGE = URL_IMAGE;
    }

    public int getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(int IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public String getDATE_UNIQUE() {
        return DATE_UNIQUE;
    }

    public void setDATE_UNIQUE(String DATE_UNIQUE) {
        this.DATE_UNIQUE = DATE_UNIQUE;
    }
}
