package com.example.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.model.Data
import com.example.myapplication.viewmodel.TracksViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun HomeScreen(navController: NavController, viewModel: TracksViewModel) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedTab by remember { mutableStateOf("All Songs") }
    var selectedItem by remember { mutableStateOf<String?>(null) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val bottomSheetHeight = screenHeight * 0.2f // 20% of the screen height

    val tabs = listOf("All Songs", "Albums", "Artists")
    val selectedTabIndex = tabs.indexOf(selectedTab)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Custom Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .shadow(8.dp, shape = RoundedCornerShape(8.dp)) // Add shadow for glass effect
                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White, // White color for the search icon
                    modifier = Modifier.padding(8.dp)
                )
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { query -> searchQuery = query },
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        if (searchQuery.text.isNotEmpty()) {
                            viewModel.searchTracks(searchQuery.text)
                        } else {
                            Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
                        }
                    })
                )
            }
        }

        Button(
            onClick = {
                if (searchQuery.text.isNotEmpty()) {
                    viewModel.searchTracks(searchQuery.text)
                } else {
                    Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Black,
            contentColor = Color.White,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color.White
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                TabButton(tab, selectedTab == tab) { selectedTab = tab }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f) // Take up remaining space
        ) {
            when (selectedTab) {
                "All Songs" -> items(viewModel.tracks) { track ->
                    TrackItem(track, navController, viewModel)
                }
                "Albums" -> items(generateSampleData("Album")) { album ->
                    ItemListItem(item = album) {
                        selectedItem = album
                        isBottomSheetVisible = true
                    }
                }
                "Artists" -> items(generateSampleData("Artist")) { artist ->
                    ItemListItem(item = artist) {
                        selectedItem = artist
                        isBottomSheetVisible = true
                    }
                }
            }
        }

        // Bottom Sheet without Animation
        if (isBottomSheetVisible && selectedItem != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bottomSheetHeight) // Set height to 20% of screen height
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(0.dp)
                    .border(2.dp, Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) // Add white border
                    .clickable { isBottomSheetVisible = false } // Click to dismiss
            ) {
                BottomSheetContent(
                    content = selectedItem ?: "",
                    onDismiss = { isBottomSheetVisible = false }
                )
            }
        }
    }
}

@Composable
fun TrackItem(track: Data, navController: NavController, viewModel: TracksViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.DarkGray, shape = RoundedCornerShape(8.dp)),
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
                    .border(BorderStroke(2.dp, Color.Gray), shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = track.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = track.artists.items.joinToString(", ") { it.profile.name },
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "ID: ${track.id}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Button(
                onClick = {
                    viewModel.getTrackDetails(track.id)
                    navController.navigate("trackDetails/${track.id}")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Text("Details")
            }
        }
    }
}
@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Tab(
        selected = isSelected,
        onClick = onClick,
        text = {
            Text(
                text = text,
                color = if (isSelected) Color.White else Color.Gray,
                fontSize = 18.sp
            )
        }
    )
}

@Composable
fun ItemListItem(item: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp) // Slightly reduced space between items
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = item,
            color = Color.White
        )
    }
}

@Composable
fun BottomSheetContent(content: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Height controlled by parent Box
            .background(Color.Black, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = content,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

fun generateSampleData(prefix: String): List<String> {
    return List(20) { "$prefix ${it + 1}" }
}
