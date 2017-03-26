package com.valdroide.mycitysshopsuser.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class Offer extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_OFFER_KEY;

    @Column
    @SerializedName("id_shop")
  //  @ForeignKey(references = {@ForeignKeyReference(columnName = "id_shop_foreign", columnType = Integer.class, foreignKeyColumnName = "ID_SHOP_KEY")}, tableClass = Shop.class, saveForeignKeyModel = false, onDelete = ForeignKeyAction.CASCADE, onUpdate = ForeignKeyAction.CASCADE)
    public int ID_SHOP_FOREIGN;

    @Column
    @SerializedName("title")
    public String TITLE;

    @Column
    @SerializedName("offer")
    public String OFFER;



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

}
