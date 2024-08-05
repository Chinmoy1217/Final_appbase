package com.example.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.model.Data
import com.example.myapplication.viewmodel.TracksViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun HomeScreen(navController: NavController, viewModel: TracksViewModel) {
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search for tracks") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .border(BorderStroke(1.dp, Color.Gray)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (query.isNotEmpty()) {
                    viewModel.searchTracks(query)
                } else {
                    Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
                }
            })
        )
        Button(
            onClick = {
                if (query.isNotEmpty()) {
                    viewModel.searchTracks(query)
                } else {
                    Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.tracks) { track ->
                TrackItem(track, navController, viewModel)
            }
        }
    }
}

@Composable
fun TrackItem(track: Data, navController: NavController, viewModel: TracksViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = track.albumOfTrack.coverArt.sources.firstOrNull()?.url)
                        .apply { crossfade(true) }
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .border(BorderStroke(2.dp, Color.Gray)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = track.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = track.artists.items.joinToString(", ") { it.profile.name }, fontSize = 16.sp, color = Color.Gray)
                Text(text = "ID: ${track.id}", fontSize = 14.sp, color = Color.Gray)
            }
            Button(onClick = {
                viewModel.getTrackDetails(track.id)
                navController.navigate("trackDetails/${track.id}")
            }) {
                Text("Details")
            }
        }
    }
}