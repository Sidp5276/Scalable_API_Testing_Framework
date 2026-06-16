package com.APIFramework.pojos.Groq.responsePojo;

public class Usage {

    private Integer total_tokens;
    private Integer completion_tokens;

    public Integer getTotalTokens() {
        return total_tokens;
    }

    public void setTotalTokens(Integer total_tokens) {
        this.total_tokens = total_tokens;
    }

    public Integer getCompletionTokens() {
        return completion_tokens;
    }

    public void setCompletionTokens(Integer completion_tokens) {
        this.completion_tokens = completion_tokens;
    }
}
