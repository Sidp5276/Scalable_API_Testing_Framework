package com.APIFramework.pojos.vwo.responsepojo;

import com.google.gson.annotations.SerializedName;
@SuppressWarnings("unused")

public class InvalidLoginRequest {
    @SerializedName("message")
    private String mMessage;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
