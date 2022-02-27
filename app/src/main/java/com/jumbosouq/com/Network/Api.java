package com.jumbosouq.com.Network;

import com.jumbosouq.com.Modelclass.SignInModel;
import com.jumbosouq.com.Modelclass.SignUpData;
import com.jumbosouq.com.Modelclass.SignUpModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @Headers({
            "Content-Type: application/json",
    })
    @POST("rest/default/V1/integration/customer/token")
    Call<String> signIn(@Body SignInModel body);

    @POST("rest/default/V1/integration/admin/token")
    Call<String> signInadmin(@Body SignInModel body);


    @Headers({
            "Content-Type: application/json",
    })
    @POST("rest/default/V1/customers")
    Call<SignUpData> signUp(@Body SignUpModel body);
}
