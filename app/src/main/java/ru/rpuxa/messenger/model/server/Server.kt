package ru.rpuxa.messenger.model.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.rpuxa.messenger.model.server.answers.*

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

    @GET("/friends/sendRequest")
    suspend fun sendFriendRequest(
        @Query("token") token: String,
        @Query("login") friendLogin: String
    ): Answer

    @GET("/friends/getRequests")
    suspend fun getFriendsRequests(
        @Query("token") token: String
    ): IdsAnswer

    @GET("/friends/getAll")
    suspend fun getFriends(
        @Query("token") token: String
    ): IdsAnswer

    @GET("/friends/answer")
    suspend fun requestAnswer(
        @Query("token") token: String,
        @Query("id") id: Int,
        @Query("accept") accept: Int
    ): Answer

    @GET("/dialogs/getMessages")
    suspend fun getMessages(
        @Query("token") token: String,
        @Query("id") dialogId: Int,
        @Query("messageId") offset: Int,
        @Query("limit") limit: Int
    ): List<MessageAnswer>

    @GET("/getLastActionId")
    suspend fun getLastActionId(
        @Query("token") token: String
    ): LastActionIdAnswer

    companion object {

        fun create(ip: String): Server =
            Retrofit.Builder()
                .baseUrl(ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Server::class.java)
    }
}