package com.devtau.retrofit2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayorov_O on 01.02.2017.
 */

public class LoginResponse {

    @SerializedName("token")
    String token;

    @SerializedName("event_id")
    String event_id;

    public String getToken(){
        return token;
    }

    public String getEventId(){
        return event_id;
    }

}
