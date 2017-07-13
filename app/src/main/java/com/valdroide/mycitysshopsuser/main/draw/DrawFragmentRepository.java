package com.valdroide.mycitysshopsuser.main.draw;


import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.shop.Draw;

public interface DrawFragmentRepository {
    void getDraws(Context context);
    void participate(Context context, Draw draw, String dni, String name);
    void refreshLayout(Context context);
    void getDrawSearch(Context context, String letter);
}
