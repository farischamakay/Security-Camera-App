package com.example.securicamapplication.api

import com.example.securicamapplication.data.DataResponse
import com.example.securicamapplication.data.LogIn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LogIn>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DataResponse>

//    @Headers("Content-Type: application/json")
//    @POST("auth/login")
//    suspend fun login(@Body body: LoginDataPost ): Response<LoginResponse>
//
//    @Headers("Content-Type: application/json")
//    @GET("user/all")
//    suspend fun getUsers(@Header("x-access-token") token: String): Response<AllUserResponse>

//    @Multipart
//    @POST("stories")
//    fun uploadStories(
//        @Header("Authorization") token: String,
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody
//    ): Call<AddStory>
//
//    @GET("stories")
//    fun getAllStories(
//        @Header("Authorization") token: String
//    ): Call<Stories>
}