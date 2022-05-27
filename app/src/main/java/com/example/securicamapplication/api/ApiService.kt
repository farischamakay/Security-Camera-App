package com.example.securicamapplication.api

import com.example.securicamapplication.data.DataResponse
import com.example.securicamapplication.data.LoginResult
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResult>

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): Call<DataResponse>

}