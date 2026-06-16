package com.APIFramework.pojos.Groq.responsePojo;

import com.google.gson.annotations.SerializedName;

public class GroqErrorResponse {

    @SerializedName("error")
    private ErrorBody error;

    public ErrorBody getError() {
        return error;
    }

    public void setError(ErrorBody error) {
        this.error = error;
    }

    public static class ErrorBody {

        @SerializedName("message")
        private String message;

        @SerializedName("type")
        private String type;

        @SerializedName("param")
        private String param;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }
}
