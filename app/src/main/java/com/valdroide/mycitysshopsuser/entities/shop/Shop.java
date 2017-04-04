package com.valdroide.mycitysshopsuser.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class Shop extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_SHOP_KEY;

    @Column
    @SerializedName("shop")
    public String SHOP;

    @Column
    @SerializedName("id_account")
    public int ID_ACCOUNT_FOREIGN;

    @Column
    @SerializedName("user")
    public String USER;

    @Column
    @SerializedName("pass")
    public String PASS;

    @Column
    @SerializedName("id_city")
    public int ID_CITY_FOREIGN;

    @Column
    @SerializedName("id_cat_sub")
    public int ID_CAT_SUB_FOREIGN;

    @Column
    @SerializedName("is_active")
    public int ISACTIVE;

    @Column
    @SerializedName("url_logo")
    public String URL_LOGO;

    @Column
    @SerializedName("name_logo")
    public String NAME_LOGO;

    @Column
    @SerializedName("description")
    public String DESCRIPTION;

    @Column
    @SerializedName("phone")
    public String PHONE;

    @Column
    @SerializedName("email")
    public String EMAIL;

    @Column
    @SerializedName("latitud")
    public String LATITUD;

    @Column
    @SerializedName("longitud")
    public String LONGITUD;

    @Column
    @SerializedName("adrress")
    public String ADDRESS;

    @Column
    @SerializedName("follow")
    public int FOLLOW;

    @Column
    @SerializedName("is_follow")
    public int IS_FOLLOW;

    @Column
    @SerializedName("working_hours")
    public String WORKING_HOURS;

    @Column
    @SerializedName("web")
    public String WEB;

    @Column
    @SerializedName("whatsapp")
    public String WHATSAAP;

    @Column
    @SerializedName("facebook")
    public String FACEBOOK;

    @Column
    @SerializedName("instagram")
    public String INSTAGRAM;

    @Column
    @SerializedName("twitter")
    public String TWITTER;

    @Column
    @SerializedName("snapchat")
    public String SNAPCHAT;

    @Column
    @SerializedName("date_unique")
    public String DATE_UNIQUE;

    @Column(defaultValue = "0")
    public int IS_SHOP_UPDATE;
    @Column(defaultValue = "0")
    public int IS_OFFER_UPDATE;
    public Shop() {
    }

    public int getID_SHOP_KEY() {
        return ID_SHOP_KEY;
    }

    public void setID_SHOP_KEY(int ID_SHOP_KEY) {
        this.ID_SHOP_KEY = ID_SHOP_KEY;
    }

    public int getISACTIVE() {
        return ISACTIVE;
    }

    public void setISACTIVE(int ISACTIVE) {
        this.ISACTIVE = ISACTIVE;
    }

    public String getSHOP() {
        return SHOP;
    }

    public void setSHOP(String SHOP) {
        this.SHOP = SHOP;
    }

    public int getID_ACCOUNT_FOREIGN() {
        return ID_ACCOUNT_FOREIGN;
    }

    public void setID_ACCOUNT_FOREIGN(int ID_ACCOUNT_FOREIGN) {
        this.ID_ACCOUNT_FOREIGN = ID_ACCOUNT_FOREIGN;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getPASS() {
        return PASS;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }

    public int getID_CAT_SUB_FOREIGN() {
        return ID_CAT_SUB_FOREIGN;
    }

    public void setID_CAT_SUB_FOREIGN(int ID_CAT_SUB_FOREIGN) {
        this.ID_CAT_SUB_FOREIGN = ID_CAT_SUB_FOREIGN;
    }

    public String getURL_LOGO() {
        return URL_LOGO;
    }

    public void setURL_LOGO(String URL_LOGO) {
        this.URL_LOGO = URL_LOGO;
    }

    public String getNAME_LOGO() {
        return NAME_LOGO;
    }

    public void setNAME_LOGO(String NAME_LOGO) {
        this.NAME_LOGO = NAME_LOGO;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getLATITUD() {
        return LATITUD;
    }

    public void setLATITUD(String LATITUD) {
        this.LATITUD = LATITUD;
    }

    public String getLONGITUD() {
        return LONGITUD;
    }

    public void setLONGITUD(String LONGITUD) {
        this.LONGITUD = LONGITUD;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public int getFOLLOW() {
        return FOLLOW;
    }

    public void setFOLLOW(int FOLLOW) {
        this.FOLLOW = FOLLOW;
    }

    public int getIS_FOLLOW() {
        return IS_FOLLOW;
    }

    public void setIS_FOLLOW(int IS_FOLLOW) {
        this.IS_FOLLOW = IS_FOLLOW;
    }

    public String getWORKING_HOURS() {
        return WORKING_HOURS;
    }

    public void setWORKING_HOURS(String WORKING_HOURS) {
        this.WORKING_HOURS = WORKING_HOURS;
    }

    public String getWEB() {
        return WEB;
    }

    public void setWEB(String WEB) {
        this.WEB = WEB;
    }

    public String getWHATSAAP() {
        return WHATSAAP;
    }

    public void setWHATSAAP(String WHATSAAP) {
        this.WHATSAAP = WHATSAAP;
    }

    public String getFACEBOOK() {
        return FACEBOOK;
    }

    public void setFACEBOOK(String FACEBOOK) {
        this.FACEBOOK = FACEBOOK;
    }

    public String getINSTAGRAM() {
        return INSTAGRAM;
    }

    public void setINSTAGRAM(String INSTAGRAM) {
        this.INSTAGRAM = INSTAGRAM;
    }

    public String getTWITTER() {
        return TWITTER;
    }

    public void setTWITTER(String TWITTER) {
        this.TWITTER = TWITTER;
    }

    public String getSNAPCHAT() {
        return SNAPCHAT;
    }

    public void setSNAPCHAT(String SNAPCHAT) {
        this.SNAPCHAT = SNAPCHAT;
    }

    public String getDATE_UNIQUE() {
        return DATE_UNIQUE;
    }

    public void setDATE_UNIQUE(String DATE_UNIQUE) {
        this.DATE_UNIQUE = DATE_UNIQUE;
    }

    public int getIS_SHOP_UPDATE() {
        return IS_SHOP_UPDATE;
    }

    public void setIS_SHOP_UPDATE(int IS_SHOP_UPDATE) {
        this.IS_SHOP_UPDATE = IS_SHOP_UPDATE;
    }

    public int getIS_OFFER_UPDATE() {
        return IS_OFFER_UPDATE;
    }

    public void setIS_OFFER_UPDATE(int IS_OFFER_UPDATE) {
        this.IS_OFFER_UPDATE = IS_OFFER_UPDATE;
    }
}
