package com.noble.assignment

import com.google.gson.annotations.SerializedName

class UserListResponse (

    val data: ArrayList<Data>? = null,

    ){
    data class Data(
        @field:SerializedName("ActName")
        val actName: String? = null,

        @field:SerializedName("actid")
        val actid: String? = null,
    )
}