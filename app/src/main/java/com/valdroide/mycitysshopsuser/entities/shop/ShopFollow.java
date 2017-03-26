package com.valdroide.mycitysshopsuser.entities.shop;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class ShopFollow extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int ID_SHOP_FOLLOW_KEY;
    @Column
    public int ID_SHOP_FOREIGN;
    @Column
    public int IS_SHOP_FOLLOW;
    public ShopFollow() {
    }

    public int getID_SHOP_FOLLOW_KEY() {
        return ID_SHOP_FOLLOW_KEY;
    }

    public void setID_SHOP_FOLLOW_KEY(int ID_SHOP_FOLLOW_KEY) {
        this.ID_SHOP_FOLLOW_KEY = ID_SHOP_FOLLOW_KEY;
    }

    public int getID_SHOP_FOREIGN() {
        return ID_SHOP_FOREIGN;
    }

    public void setID_SHOP_FOREIGN(int ID_SHOP_FOREIGN) {
        this.ID_SHOP_FOREIGN = ID_SHOP_FOREIGN;
    }

    public int getIS_SHOP_FOLLOW() {
        return IS_SHOP_FOLLOW;
    }

    public void setIS_SHOP_FOLLOW(int IS_SHOP_FOLLOW) {
        this.IS_SHOP_FOLLOW = IS_SHOP_FOLLOW;
    }
}
