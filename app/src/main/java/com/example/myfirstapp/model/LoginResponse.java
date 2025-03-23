package com.example.myfirstapp.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("msg")
    private String msg;

    @SerializedName("token")
    private String token;

    // Getters
    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public String getToken() { return token; }
}