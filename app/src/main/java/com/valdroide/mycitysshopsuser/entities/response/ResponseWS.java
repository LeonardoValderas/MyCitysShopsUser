package com.valdroide.mycitysshopsuser.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWS {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("success")
    @Expose
    String success;
    @SerializedName("message")
    @Expose
    String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
