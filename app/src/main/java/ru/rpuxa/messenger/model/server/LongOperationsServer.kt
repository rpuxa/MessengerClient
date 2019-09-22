package ru.rpuxa.messenger.model.server

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.rpuxa.messenger.model.server.answers.ActionsAnswer
import ru.rpuxa.messenger.model.server.answers.UrlAnswer
import java.util.concurrent.TimeUnit

interface LongOperationsServer {


    @Multipart
    @POST("/profile/setAvatar")
    suspend fun setAvatar(
        @Query("token") token: String,
        @Part icon: MultipartBody.Part
    ): UrlAnswer

    @GET("/actions/get")
    suspend fun getActions(
        @Query("token") token: String,
        @Query("last") lastActionsId: Int
    ): ActionsAnswer

    companion object {
        fun create(ip: String): LongOperationsServer =
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
                .create(LongOperationsServer::class.java)
    }
}