package com.example.securicamapplication.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataResponse (
    @field:SerializedName("success")
    var success : Boolean? = null,

    @field:SerializedName("message")
    var message : String?  = null,

    @field:SerializedName("data") var data    : LogIn?    = LogIn()
) : Parcelable
