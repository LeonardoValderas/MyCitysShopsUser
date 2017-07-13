package com.valdroide.mycitysshopsuser.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class Draw extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    private int ID_DRAW_KEY;

    @Column
    @SerializedName("id_shop")
    public int ID_SHOP_FOREIGN_DRAW;
    @Column
    @SerializedName("id_city")
    public int ID_CITY_FOREIGN;

    @Column
    @SerializedName("description")
    private String DESCRIPTION;

    @Column
    @SerializedName("for_following")
    private int FOR_FOLLOWING;

    @Column
    @SerializedName("conditions")
    private String CONDITION;

    @Column
    @SerializedName("limite_date")
    private String LIMITE_DATE;

    @Column
    @SerializedName("end_date")
    private String END_DATE;

    @Column
    @SerializedName("url_logo")
    private String URL_LOGO;

    @Column
    @SerializedName("is_active")
    private int IS_ACTIVE;

    @SerializedName("is_take")
    private int IS_TAKE;

    @SerializedName("is_limite")
    private int IS_LIMITE;

    @Column
    @SerializedName("is_winner")
    private int IS_WINNER;
    @Column
    @SerializedName("participation")
    private int PARTICIPATION;

    @Column
    @SerializedName("date_unique")
    private String DATE_UNIQUE;

    @Column
    @SerializedName("name")
    private String SHOP_NAME;

    @Column
    @SerializedName("url_shop")
    private String URL_SHOP;

    public Draw() {
    }

    public Draw(int ID_DRAW_KEY, int ID_SHOP_FOREIGN, int ID_CITY_FOREIGN, String DESCRIPTION,
                int FOR_FOLLOWING, String CONDITION, String LIMITE_DATE, String END_DATE,
                String URL_LOGO, String DATE_UNIQUE, String SHOP_NAME, int PARTICIPATION, String URL_SHOP,
                int IS_WINNER, int IS_TAKE, int IS_LIMITE) {

        this.ID_DRAW_KEY = ID_DRAW_KEY;
        this.ID_SHOP_FOREIGN_DRAW = ID_SHOP_FOREIGN;
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
        this.DESCRIPTION = DESCRIPTION;
        this.FOR_FOLLOWING = FOR_FOLLOWING;
        this.CONDITION = CONDITION;
        this.LIMITE_DATE = LIMITE_DATE;
        this.END_DATE = END_DATE;
        this.URL_LOGO = URL_LOGO;
        this.DATE_UNIQUE = DATE_UNIQUE;
        this.SHOP_NAME = SHOP_NAME;
        this.PARTICIPATION = PARTICIPATION;
        this.URL_SHOP = URL_SHOP;
        this.IS_WINNER = IS_WINNER;
        this.IS_TAKE = IS_TAKE;
        this.IS_LIMITE = IS_LIMITE;
    }

    public int getID_DRAW_KEY() {
        return ID_DRAW_KEY;
    }

    public void setID_DRAW_KEY(int ID_DRAW_KEY) {
        this.ID_DRAW_KEY = ID_DRAW_KEY;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public int getFOR_FOLLOWING() {
        return FOR_FOLLOWING;
    }

    public void setFOR_FOLLOWING(int FOR_FOLLOWING) {
        this.FOR_FOLLOWING = FOR_FOLLOWING;
    }

    public String getCONDITION() {
        return CONDITION;
    }

    public void setCONDITION(String CONDITION) {
        this.CONDITION = CONDITION;
    }

    public String getLIMITE_DATE() {
        return LIMITE_DATE;
    }

    public void setLIMITE_DATE(String LIMITE_DATE) {
        this.LIMITE_DATE = LIMITE_DATE;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getURL_LOGO() {
        return URL_LOGO;
    }

    public void setURL_LOGO(String URL_LOGO) {
        this.URL_LOGO = URL_LOGO;
    }

    public String getDATE_UNIQUE() {
        return DATE_UNIQUE;
    }

    public void setDATE_UNIQUE(String DATE_UNIQUE) {
        this.DATE_UNIQUE = DATE_UNIQUE;
    }

    public int getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(int IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public int getID_SHOP_FOREIGN_DRAW() {
        return ID_SHOP_FOREIGN_DRAW;
    }

    public void setID_SHOP_FOREIGN_DRAW(int ID_SHOP_FOREIGN_DRAW) {
        this.ID_SHOP_FOREIGN_DRAW = ID_SHOP_FOREIGN_DRAW;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }

    public String getSHOP_NAME() {
        return SHOP_NAME;
    }

    public void setSHOP_NAME(String SHOP_NAME) {
        this.SHOP_NAME = SHOP_NAME;
    }

    public int getPARTICIPATION() {
        return PARTICIPATION;
    }

    public void setPARTICIPATION(int PARTICIPATION) {
        this.PARTICIPATION = PARTICIPATION;
    }

    public String getURL_SHOP() {
        return URL_SHOP;
    }

    public void setURL_SHOP(String URL_SHOP) {
        this.URL_SHOP = URL_SHOP;
    }

    public int getIS_WINNER() {
        return IS_WINNER;
    }

    public void setIS_WINNER(int IS_WINNER) {
        this.IS_WINNER = IS_WINNER;
    }

    public int getIS_TAKE() {
        return IS_TAKE;
    }

    public void setIS_TAKE(int IS_TAKE) {
        this.IS_TAKE = IS_TAKE;
    }

    public int getIS_LIMITE() {
        return IS_LIMITE;
    }

    public void setIS_LIMITE(int IS_LIMITE) {
        this.IS_LIMITE = IS_LIMITE;
    }
}
