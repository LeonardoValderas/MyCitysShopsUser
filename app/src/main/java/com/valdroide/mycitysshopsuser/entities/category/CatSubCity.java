package com.valdroide.mycitysshopsuser.entities.category;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class CatSubCity extends BaseModel {
    //  @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    private int ID_CAT_SUB_KEY;
    @Column
    @SerializedName("id_category")
    private int ID_CATEGORY_FOREIGN;
    @Column
    @SerializedName("id_subcategory")
    private int ID_SUBCATEGORY_FOREIGN;
    @Column
    @SerializedName("id_city")
    private int ID_CITY_FOREIGN;

     public CatSubCity() {
    }

    public int getID_CAT_SUB_KEY() {
        return ID_CAT_SUB_KEY;
    }

    public void setID_CAT_SUB_KEY(int ID_CAT_SUB_KEY) {
        this.ID_CAT_SUB_KEY = ID_CAT_SUB_KEY;
    }

    public int getID_CATEGORY_FOREIGN() {
        return ID_CATEGORY_FOREIGN;
    }

    public void setID_CATEGORY_FOREIGN(int ID_CATEGORY_FOREIGN) {
        this.ID_CATEGORY_FOREIGN = ID_CATEGORY_FOREIGN;
    }

    public int getID_SUBCATEGORY_FOREIGN() {
        return ID_SUBCATEGORY_FOREIGN;
    }

    public void setID_SUBCATEGORY_FOREIGN(int ID_SUBCATEGORY_FOREIGN) {
        this.ID_SUBCATEGORY_FOREIGN = ID_SUBCATEGORY_FOREIGN;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }
}
