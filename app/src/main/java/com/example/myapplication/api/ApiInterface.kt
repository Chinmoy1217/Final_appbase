package com.example.myapplication.api


import com.example.myapplication.model.TrackDetailsResponse
import com.example.myapplication.model.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @Headers(
        "x-rapidapi-key:6abaafc88dmsh81cb72f07b2443ap1be866jsnbdf3ed7d9ced",
        "x-rapidapi-host:spotify23.p.rapidapi.com"
    )
    @GET("search")
    fun search(
        @Query("q") query: String,
        @Query("type") type: String = "multi",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<TracksResponse>

    @Headers(
        "x-rapidapi-key:6abaafc88dmsh81cb72f07b2443ap1be866jsnbdf3ed7d9ced",
        "x-rapidapi-host:spotify23.p.rapidapi.com"
    )
    @GET("tracks")
    fun getTracks(
        @Query("ids") ids: String
    ): Call<TrackDetailsResponse>
}