package com.valdroide.mycitysshopsuser.entities.shop;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;

@Table(database = ShopsDatabase.class)
public class DrawWinner extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    private int ID_DRAW_WINNER_KEY;

    @Column
    public int ID_CITY_FOREIGN;

    @Column
    private int ID_DRAW_FOREIGN;

    public DrawWinner() {
    }

    public DrawWinner(int ID_CITY_FOREIGN, int ID_DRAW_FOREIGN) {
      this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
        this.ID_DRAW_FOREIGN = ID_DRAW_FOREIGN;
    }

    public int getID_DRAW_WINNER_KEY() {
        return ID_DRAW_WINNER_KEY;
    }

    public void setID_DRAW_WINNER_KEY(int ID_DRAW_WINNER_KEY) {
        this.ID_DRAW_WINNER_KEY = ID_DRAW_WINNER_KEY;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }

    public int getID_DRAW_FOREIGN() {
        return ID_DRAW_FOREIGN;
    }

    public void setID_DRAW_FOREIGN(int ID_DRAW_FOREIGN) {
        this.ID_DRAW_FOREIGN = ID_DRAW_FOREIGN;
    }
}
