package com.example.myapplication.respository

import android.content.Context
import com.example.myapplication.api.ApiInterface
import com.example.myapplication.model.Data
import com.example.myapplication.model.Track
import com.example.myapplication.model.TrackDetailsResponse
import com.example.myapplication.model.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TrackRepository(private val context: Context) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://spotify23.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiInterface = retrofit.create(ApiInterface::class.java)

    fun searchTracks(query: String, onResult: (List<Data>) -> Unit, onError: (String) -> Unit) {
        val call = apiInterface.search(query)

        call.enqueue(object : Callback<TracksResponse> {
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                if (response.isSuccessful) {
                    val tracksList = response.body()?.tracks?.items?.map { it.data }
                    if (tracksList != null) {
                        onResult(tracksList)
                    } else {
                        onError("No track results found")
                    }
                } else {
                    onError("Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                onError("Failed to fetch data: ${t.message}")
            }
        })
    }

    fun getTrackDetails(trackId: String, onResult: (Track) -> Unit, onError: (String) -> Unit) {
        val call = apiInterface.getTracks(trackId)

        call.enqueue(object : Callback<TrackDetailsResponse> {
            override fun onResponse(call: Call<TrackDetailsResponse>, response: Response<TrackDetailsResponse>) {
                if (response.isSuccessful) {
                    val trackDetailsList = response.body()?.tracks
                    if (!trackDetailsList.isNullOrEmpty()) {
                        onResult(trackDetailsList.first())
                    } else {
                        onError("No details found")
                    }
                } else {
                    onError("Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TrackDetailsResponse>, t: Throwable) {
                onError("Failed to fetch data: ${t.message}")
            }
        })
    }
}