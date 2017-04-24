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
    public int ID_DATE_KEY;
    @Column
    @SerializedName("id_city")
    public int ID_CITY_FOREIGN;
    @Column
    @SerializedName("category")
    public String CATEGORY_DATE;
    @Column
    @SerializedName("subcategory")
    public String SUBCATEGORY_DATE;
    @Column
    @SerializedName("cat_sub_city")
    public String CAT_SUB_CITY_DATE;
    @Column
    @SerializedName("shop")
    public String SHOP_DATE;
    @Column
    @SerializedName("offer")
    public String OFFER_DATE;
    @Column
    @SerializedName("date_user")
    public String DATE_USER_CITY;

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

    public String getCAT_SUB_CITY_DATE() {
        return CAT_SUB_CITY_DATE;
    }

    public void setCAT_SUB_CITY_DATE(String CAT_SUB_CITY_DATE) {
        this.CAT_SUB_CITY_DATE = CAT_SUB_CITY_DATE;
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
}
