package com.valdroide.mycitysshopsuser.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class DateUserCity extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    private int ID_DATE_KEY;
    @Column
    @SerializedName("id_city")
    private int ID_CITY_FOREIGN;
    @Column
    @SerializedName("category")
    private String CATEGORY_DATE;
    @Column
    @SerializedName("subcategory")
    private String SUBCATEGORY_DATE;
    @Column
    @SerializedName("shop")
    private String SHOP_DATE;
    @Column
    @SerializedName("offer")
    private String OFFER_DATE;
    @Column
    @SerializedName("draw")
    private String DRAW_DATE;
    @Column
    @SerializedName("support")
    private String SUPPORT_DATE;
    @Column
    @SerializedName("date_user")
    private String DATE_USER_CITY;

    public DateUserCity() {
    }

    public int getID_DATE_KEY() {
        return ID_DATE_KEY;
    }

    public void setID_DATE_KEY(int ID_DATE_KEY) {
        this.ID_DATE_KEY = ID_DATE_KEY;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }

    public String getCATEGORY_DATE() {
        return CATEGORY_DATE;
    }

    public void setCATEGORY_DATE(String CATEGORY_DATE) {
        this.CATEGORY_DATE = CATEGORY_DATE;
    }

    public String getSUBCATEGORY_DATE() {
        return SUBCATEGORY_DATE;
    }

    public void setSUBCATEGORY_DATE(String SUBCATEGORY_DATE) {
        this.SUBCATEGORY_DATE = SUBCATEGORY_DATE;
    }

    public String getSHOP_DATE() {
        return SHOP_DATE;
    }

    public void setSHOP_DATE(String SHOP_DATE) {
        this.SHOP_DATE = SHOP_DATE;
    }

    public String getOFFER_DATE() {
        return OFFER_DATE;
    }

    public void setOFFER_DATE(String OFFER_DATE) {
        this.OFFER_DATE = OFFER_DATE;
    }

    public String getDATE_USER_CITY() {
        return DATE_USER_CITY;
    }

    public void setDATE_USER_CITY(String DATE_USER_CITY) {
        this.DATE_USER_CITY = DATE_USER_CITY;
    }

    public String getSUPPORT_DATE() {
        return SUPPORT_DATE;
    }

    public void setSUPPORT_DATE(String SUPPORT_DATE) {
        this.SUPPORT_DATE = SUPPORT_DATE;
    }

    public String getDRAW_DATE() {
        return DRAW_DATE;
    }

    public void setDRAW_DATE(String DRAW_DATE) {
        this.DRAW_DATE = DRAW_DATE;
    }
}
