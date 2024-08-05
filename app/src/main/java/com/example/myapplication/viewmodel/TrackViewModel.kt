package com.example.myapplication.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.myapplication.model.Data
import com.example.myapplication.model.Track
import com.example.myapplication.respository.TrackRepository

class TracksViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    private val repository = TrackRepository(context)

    val tracks = mutableStateListOf<Data>()
    var trackDetails = mutableStateOf<Track?>(null)
    private var exoPlayer: ExoPlayer? = null
    var isPlaying = mutableStateOf(false)

    fun searchTracks(query: String) {
        repository.searchTracks(query,
            onResult = { tracksList ->
                tracks.clear()
                tracks.addAll(tracksList)
            },
            onError = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun getTrackDetails(trackId: String) {
        repository.getTrackDetails(trackId,
            onResult = { track ->
                trackDetails.value = track
            },
            onError = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun togglePlayback(previewUrl: String?) {
        if (previewUrl.isNullOrEmpty()) {
            Toast.makeText(context, "Preview URL is not available", Toast.LENGTH_SHORT).show()
            return
        }

        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }

        exoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                isPlaying.value = false
            } else {
                // If the ExoPlayer is not playing, we need to check if it's already prepared for the given URL
                // If it's the same URL, just resume; otherwise, prepare the new media item.
                val currentMediaItem = player.currentMediaItem
                if (currentMediaItem == null || currentMediaItem.mediaId != previewUrl) {
                    player.setMediaItem(MediaItem.fromUri(previewUrl))
                    player.prepare()
                }
                player.play()
                isPlaying.value = true
            }
        }
    }

    public override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
    }
}