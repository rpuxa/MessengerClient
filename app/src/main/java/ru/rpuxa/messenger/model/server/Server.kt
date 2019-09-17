package ru.rpuxa.messenger.model.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.rpuxa.messenger.model.server.answers.PrivateInfoAnswer
import ru.rpuxa.messenger.model.server.answers.PublicInfoAnswer
import ru.rpuxa.messenger.model.server.answers.SetInfoAnswer
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


    @GET("/profile/getPrivateInfo")
    suspend fun getPrivateInfo(
        @Query("token") token: String
    ): PrivateInfoAnswer


    @GET("/profile/getPublicInfo")
    suspend fun getPublicInfo(
        @Query("id") id: Int
    ): PublicInfoAnswer


    @FormUrlEncoded
    @POST("/profile/setInfo")
    suspend fun setInfo(
        @Query("token") token: String,
        @FieldMap(encoded = true) fields: Map<String, String>
    ): SetInfoAnswer


    companion object {

        fun create(ip: String): Server =
            Retrofit.Builder()
                .baseUrl(ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Server::class.java)
    }
}