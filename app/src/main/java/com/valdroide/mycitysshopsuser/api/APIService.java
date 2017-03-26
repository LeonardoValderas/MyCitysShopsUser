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

/*
    @FormUrlEncoded
    @POST("account/updateAccount.php")
    Call<ResultPlace> updateAccount(@Field("id_user_foreign") int id_user, @Field("id_account") int id_account, @Field("encode") String encode,
                                    @Field("url_logo") String url_logo, @Field("name_logo") String name_logo, @Field("name_before") String name_before,
                                    @Field("description") String description, @Field("phone") String phone, @Field("email") String email,
                                    @Field("latitud") String latitud, @Field("longitud") String longitud, @Field("adrress") String adrress, @Field("date_init") String date_init);
    //OFFER
    @FormUrlEncoded
    @POST("offer/insertOffer.php")
    Call<ResultPlace> insertOffer(@Field("id_user_foreign") int id_user, @Field("title") String title, @Field("offer") String offer,
                                  @Field("date_init") String date_init, @Field("date_end") String date_end);

    @FormUrlEncoded
    @POST("offer/updateOffer.php")
    Call<ResultPlace> updateOffer(@Field("id_offer") int id_offer, @Field("id_user_foreign") int id_user, @Field("title") String title, @Field("offer") String offer,
                                  @Field("date_edit") String date_edit);

    @FormUrlEncoded
    @POST("offer/deleteOffer.php")
    Call<ResultPlace> deleteOffer(@Field("id_offer") int id_offer, @Field("id_user_foreign") int id_user, @Field("date_edit") String date_edit);

    //NOTIFICATION
    @FormUrlEncoded
    @POST("fcm/sendNotification.php")
    Call<ResultPlace> sendNotification(@Field("id_user_foreign") int id_user, @Field("title") String title, @Field("message") String message, @Field("date_init") String date_init);
*/
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
                                      @Field("date_user") String date_user);

    @FormUrlEncoded
    @POST("splash_user/getShopData.php")
    Call<ResultShop> getShop(@Field("id_city") int id_city);

    //FOLLOW
    @FormUrlEncoded
    @POST("follow/follow.php")
    Call<ResponseWS> follow(@Field("id_shop") int id_shop, @Field("id_city") int id_city,
                            @Field("date_update") String date_update,
                            @Field("is_follow") int is_follow);

}
