package com.APIFramework.pojos.Groq.responsePojo;

public class Choice {

    private ChatMessage message;
    private String finish_reason;

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public String getFinishReason() {
        return finish_reason;
    }

    public void setFinishReason(String finish_reason) {
        this.finish_reason = finish_reason;
    }
}
