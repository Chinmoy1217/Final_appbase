package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.HomeScreen
import com.example.myapplication.screens.TrackDetailsScreen
import com.example.myapplication.viewmodel.TracksViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: TracksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "home") {
            composable("home")
            { HomeScreen(navController, viewModel) }

            composable("trackDetails/{trackId}")
            { backStackEntry ->
                val trackId = backStackEntry.arguments?.getString("trackId")
                TrackDetailsScreen(navController, trackId, viewModel)
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
    }
}
