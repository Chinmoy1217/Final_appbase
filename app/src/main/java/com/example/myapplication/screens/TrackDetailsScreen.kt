package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myapplication.viewmodel.TracksViewModel
import com.example.myapplication.R

@Composable
fun TrackDetailsScreen(navController: NavController, trackId: String?, viewModel: TracksViewModel) {
    val trackDetail by viewModel.trackDetails

    LaunchedEffect(trackId) {
        trackId?.let {
            viewModel.getTrackDetails(it)
        }
    }

    trackDetail?.let { track ->
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = track.album.images.firstOrNull()?.url)
                        .apply { crossfade(true) }
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = track.name, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text(text = track.artists.joinToString(", ") { it.name }, fontSize = 20.sp, color = Color.Gray)
            Text(text = "Album: ${track.album.name}", fontSize = 20.sp, color = Color.Gray)
            Text(text = "ID: ${track.id}", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = {
                    viewModel.togglePlayback(trackDetail?.preview_url)
                }) {
                    Icon(
                        painter = painterResource(id = if (viewModel.isPlaying.value) R.drawable.pause else R.drawable.play_arrow),
                        contentDescription = if (viewModel.isPlaying.value) "Pause" else "Play",
                        modifier = Modifier.size(64.dp)
                    )
                }
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
    }
}