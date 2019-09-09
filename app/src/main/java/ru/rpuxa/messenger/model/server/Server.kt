package ru.rpuxa.messenger.model.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.rpuxa.messenger.model.server.answers.TokenAnswer

interface Server {

    @GET("/reg")
    suspend fun reg(
        @Query("login") login: String,
        @Query("pass") password: String,
        @Query("name") name: String,
        @Query("surname") surname: String
    ): TokenAnswer


    @GET("/login")
    suspend fun login(
        @Query("login") login: String,
        @Query("pass") password: String
    ): TokenAnswer

    companion object {

        fun create(ip: String): Server =
            Retrofit.Builder()
                .baseUrl(ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Server::class.java)
    }
}