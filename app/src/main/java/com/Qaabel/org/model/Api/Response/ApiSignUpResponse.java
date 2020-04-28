package com.Qaabel.org.model.Api.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiSignUpResponse
{
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("authToken")
    @Expose
    private String authToken;
    @SerializedName("code")
    @Expose
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public String getCode(){
        return code;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }
}
