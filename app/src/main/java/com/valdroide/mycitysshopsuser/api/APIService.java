package com.valdroide.mycitysshopsuser.api;

import com.valdroide.mycitysshopsuser.entities.response.ResponseWS;
import com.valdroide.mycitysshopsuser.entities.response.ResultPlace;
import com.valdroide.mycitysshopsuser.entities.response.ResultShop;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface APIService {

    //SPLASH PLACE
    @FormUrlEncoded
    @POST("splash/validateDatePlace.php")
    Call<ResultPlace> validateDatePlace(@Field("date_country") String date_country,
                                        @Field("date_state") String date_state,
                                        @Field("date_city") String date_city,
                                        @Field("date_place") String date_place);

    @GET("splash/getPlace.php")
    Call<ResultPlace> getPlace();

    //SPLASH USER
    @FormUrlEncoded
    @POST("splash_user/validateDateShop.php")
    Call<ResultShop> validateDateUser(@Field("id_city") int id_city, @Field("date_category") String date_category,
                                      @Field("date_subcategory") String date_subcategory,
                                      @Field("date_cat_sub_city") String date_cat_sub_city,
                                      @Field("date_shop") String date_shop,
                                      @Field("date_offer") String date_offer,
                                      @Field("date_support") String date_support,
                                      @Field("date_user") String date_user);

    @FormUrlEncoded
    @POST("splash_user/getShopData.php")
    Call<ResultShop> getShop(@Field("id_city") int id_city);

    //FOLLOW
    @FormUrlEncoded
    @POST("follow/follow.php")
    Call<ResponseWS> followOrUnFollow(@Field("id_shop") int id_shop, @Field("id_city") int id_city,
                            @Field("date_update") String date_update,
                            @Field("is_follow") boolean is_follow, @Field("id_token") int id_token);

    //TOKEN
    @FormUrlEncoded
    @POST("fcm/setToken.php")
    Call<ResponseWS> setToken(@Field("id") int id, @Field("token") String token, @Field("id_city") int id_city,
                              @Field("is_insert") boolean is_insert);
}
