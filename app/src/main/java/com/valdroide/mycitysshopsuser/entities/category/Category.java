package com.valdroide.mycitysshopsuser.entities.category;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class Category extends BaseModel {

   // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    private int ID_CATEGORY_KEY;

    @Column
    @SerializedName("category")
    private String CATEGORY;

    public Category() {
    }

    public Category(int id, String category) {
        this.ID_CATEGORY_KEY = id;
        this.CATEGORY = category;
    }

    public int getID_CATEGORY_KEY() {
        return ID_CATEGORY_KEY;
    }

    public void setID_CATEGORY_KEY(int ID_CATEGORY_KEY) {
        this.ID_CATEGORY_KEY = ID_CATEGORY_KEY;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public String toString() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }
}
