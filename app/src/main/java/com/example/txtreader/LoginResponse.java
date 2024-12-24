package com.example.txtreader;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private int userId;

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }
}