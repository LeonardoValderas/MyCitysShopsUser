package com.valdroide.mycitysshopsuser.entities.category;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class SubCategory extends BaseModel {
    //  @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    private int ID_SUBCATEGORY_KEY;
    @Column
    @SerializedName("subcategory")
    private String SUBCATEGORY;
    @Column
    @SerializedName("id_category")
    private int ID_CATEGORY;

    private String CATEGORY;

    public SubCategory() {
    }
    public SubCategory(int id, String subCategory) {
        this.ID_SUBCATEGORY_KEY = id;
        this.SUBCATEGORY = subCategory;
    }
    public SubCategory(int id, String subCategory, int id_category) {
        this.ID_SUBCATEGORY_KEY = id;
        this.SUBCATEGORY = subCategory;
        this.ID_CATEGORY = id_category;
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

    public int getID_CATEGORY() {
        return ID_CATEGORY;
    }

    public void setID_CATEGORY(int ID_CATEGORY) {
        this.ID_CATEGORY = ID_CATEGORY;
    }
}
