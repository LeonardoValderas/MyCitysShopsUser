package com.valdroide.mycitysshopsuser.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopClient {
    private Retrofit retrofit;
    //   private final static String BASE_URL = "http://10.0.2.2:8080/my_citys_shops_user/";
    //private final static String BASE_URL = "http://10.0.3.2:8080/my_citys_shops_user/";

    //DEBUG
    //private final static String BASE_URL = "http://mycitysshops.esy.es/deb/my_citys_shops_user/";
    //SERVIDOR
    private final static String BASE_URL = "http://mycitysshops.esy.es/my_citys_shops_user/";

    public ShopClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(setTimeOut())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public OkHttpClient setTimeOut() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public APIService getAPIService() {
        return retrofit.create(APIService.class);
    }
}
