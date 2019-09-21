package ru.rpuxa.messenger.model.server

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.rpuxa.messenger.model.server.answers.*
import java.io.File
import java.util.concurrent.TimeUnit

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


    @GET("/profile/setInfo")
    suspend fun setInfo(
        @Query("token") token: String,
        @QueryMap fields: Map<String, String>
    ): SetInfoAnswer


    @Multipart
    @POST("/profile/setAvatar")
    suspend fun setAvatar(
        @Query("token") token: String,
        @Part icon: MultipartBody.Part
    ): UrlAnswer

    companion object {

        fun create(ip: String): Server =
            Retrofit.Builder()
                .client(
                    OkHttpClient.Builder()
                        .writeTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)
                        .build()
                )
                .baseUrl(ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Server::class.java)
    }
}