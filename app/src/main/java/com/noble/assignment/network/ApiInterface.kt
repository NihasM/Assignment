package com.noble.assignment.network

import com.noble.assignment.UserListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("Fillaccounts/nadc/2024-2025")
    suspend fun userList() :Response<String>

}