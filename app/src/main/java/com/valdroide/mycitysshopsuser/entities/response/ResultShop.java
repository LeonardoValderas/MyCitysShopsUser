package com.valdroide.mycitysshopsuser.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.entities.shop.Support;

import java.util.List;

public class ResultShop {
    @SerializedName("responseWS")
    @Expose
    ResponseWS responseWS;
    @SerializedName("date_shop")
    @Expose
    DateUserCity dateUserCity;
    @SerializedName("category")
    @Expose
    List<Category> categories;
    @SerializedName("subcategory")
    @Expose
    List<SubCategory> subCategories;
    @SerializedName("shop")
    @Expose
    List<Shop> shops;
    @SerializedName("offer")
    @Expose
    List<Offer> offers;
    @SerializedName("draw")
    @Expose
    List<Draw> draws;
    @SerializedName("support")
    @Expose
    Support support;
    @SerializedName("ids_shops")
    @Expose
    List<Integer> idsShops;
    @SerializedName("ids_offers")
    @Expose
    List<Integer> idsOffers;

    public ResponseWS getResponseWS() {
        return responseWS;
    }

    public void setResponseWS(ResponseWS responseWS) {
        this.responseWS = responseWS;
    }

    public DateUserCity getDateUserCity() {
        return dateUserCity;
    }

    public void setDateUserCity(DateUserCity dateUserCity) {
        this.dateUserCity = dateUserCity;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }

    public List<Draw> getDraws() {
        return draws;
    }

    public void setDraws(List<Draw> draws) {
        this.draws = draws;
    }

    public List<Integer> getIdsShops() {
        return idsShops;
    }

    public void setIdsShops(List<Integer> idsShops) {
        this.idsShops = idsShops;
    }

    public List<Integer> getIdsOffers() {
        return idsOffers;
    }

    public void setIdsOffers(List<Integer> idsOffers) {
        this.idsOffers = idsOffers;
    }
}
