package com.valdroide.mycitysshopsuser.entities.category;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class SubCategory extends BaseModel {
    @Column
    @PrimaryKey
    @SerializedName("id")
    private int ID_SUBCATEGORY_KEY;
    @Column
    @SerializedName("subcategory")
    private String SUBCATEGORY;
    @Column
    @SerializedName("id_category")
    private int ID_CATEGORY_FOREIGN;

    private String CATEGORY;

    @Column
    private int IS_UPDATE;

    public SubCategory() {
    }
    public SubCategory(int id, String subCategory) {
        this.ID_SUBCATEGORY_KEY = id;
        this.SUBCATEGORY = subCategory;
    }
    public SubCategory(int id, String subCategory, int id_category) {
        this.ID_SUBCATEGORY_KEY = id;
        this.SUBCATEGORY = subCategory;
        this.ID_CATEGORY_FOREIGN = id_category;
    }
    public int getID_SUBCATEGORY_KEY() {
        return ID_SUBCATEGORY_KEY;
    }

    public void setID_SUBCATEGORY_KEY(int ID_SUBCATEGORY_KEY) {
        this.ID_SUBCATEGORY_KEY = ID_SUBCATEGORY_KEY;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public String getSUBCATEGORY() {
        return SUBCATEGORY;
    }
    public String toString() {
        return SUBCATEGORY;
    }

    public void setSUBCATEGORY(String SUBCATEGORY) {
        this.SUBCATEGORY = SUBCATEGORY;
    }

    public int getID_CATEGORY_FOREIGN() {
        return ID_CATEGORY_FOREIGN;
    }

    public void setID_CATEGORY_FOREIGN(int ID_CATEGORY_FOREIGN) {
        this.ID_CATEGORY_FOREIGN = ID_CATEGORY_FOREIGN;
    }

    public int getIS_UPDATE() {
        return IS_UPDATE;
    }

    public void setIS_UPDATE(int IS_UPDATE) {
        this.IS_UPDATE = IS_UPDATE;
    }
}
