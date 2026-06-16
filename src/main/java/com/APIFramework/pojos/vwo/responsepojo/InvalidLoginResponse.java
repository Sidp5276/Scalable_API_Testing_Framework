package com.APIFramework.pojos.vwo.responsepojo;

import com.google.gson.annotations.SerializedName;

public class InvalidLoginResponse {

    @SerializedName("message")
    private String mMessage;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }
}
