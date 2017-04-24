package com.valdroide.mycitysshopsuser.entities.place;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class MyPlace extends BaseModel {

    @Column
    @PrimaryKey
    private int ID_PLACE_KEY;
    @Column
    private int ID_COUNTRY_FOREIGN;
    @Column
    private int ID_STATE_FOREIGN;
    @Column
    private int ID_CITY_FOREIGN;

    public MyPlace() {
    }

    public int getID_PLACE_KEY() {
        return ID_PLACE_KEY;
    }

    public void setID_PLACE_KEY(int ID_PLACE_KEY) {
        this.ID_PLACE_KEY = ID_PLACE_KEY;
    }

    public int getID_COUNTRY_FOREIGN() {
        return ID_COUNTRY_FOREIGN;
    }

    public void setID_COUNTRY_FOREIGN(int ID_COUNTRY_FOREIGN) {
        this.ID_COUNTRY_FOREIGN = ID_COUNTRY_FOREIGN;
    }

    public int getID_STATE_FOREIGN() {
        return ID_STATE_FOREIGN;
    }

    public void setID_STATE_FOREIGN(int ID_STATE_FOREIGN) {
        this.ID_STATE_FOREIGN = ID_STATE_FOREIGN;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }
}
