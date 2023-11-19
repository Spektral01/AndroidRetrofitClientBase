package com.example.testrestretrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/api/User")
    fun getUser(): Call<List<User>>

    @POST("/api/User")
    fun createUser(@Body user: User): Call<MyResponse>

    @DELETE("/api/User")
    fun deleteUser(): Call<Void>
}