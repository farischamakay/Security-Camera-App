package com.example.securicamapplication.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Data?    = Data()

) : Parcelable

@Parcelize
data class Data(

    @SerializedName("id"           ) var id           : String?           = null,
    @SerializedName("username"     ) var username     : String?           = null,
    @SerializedName("email"        ) var email        : String?           = null,
    @SerializedName("password"     ) var password     : String?           = null,
    @SerializedName("role"         ) var role         : String?           = null,
    @SerializedName("fcm"          ) var fcm          : String?           = null,
    @SerializedName("lastLoggedIn" ) var lastLoggedIn : String?              = null,
    @SerializedName("connection"   ) var connection   : ArrayList<String> = arrayListOf(),
    @SerializedName("tokenExpired" ) var tokenExpired : Int?              = null,
    @SerializedName("accessToken"  ) var accessToken  : String?           = null
) : Parcelable
