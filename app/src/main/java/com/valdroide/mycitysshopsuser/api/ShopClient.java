package com.valdroide.mycitysshopsuser.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopClient {
    private Retrofit retrofit;
 //   private final static String BASE_URL = "http://10.0.2.2:8080/my_citys_shops_user/";
    //private final static String BASE_URL = "http://10.0.3.2:8080/my_citys_shops_user/";
    private final static String BASE_URL = "http://myd.esy.es/my_citys_shops_user/";

    public ShopClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public APIService getAPIService() {
        return retrofit.create(APIService.class);
    }
}
