package com.magicbluepenguin.foursquareapp.application

import VenueDetailScreen
import VenueSearchScreen
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.magicbluepenguin.foursquareapp.venuesearch.detail.VENUE_ID_PARAM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FourSquareApp()
            }
        }
    }
}

@Composable
fun FourSquareApp() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "venueSearch") {
        composable("venueSearch") {
            VenueSearchScreen {
                navController.navigate("venueDetail/$it")
            }
        }
        composable(
            "venueDetail/{$VENUE_ID_PARAM}",
            arguments = listOf(navArgument(VENUE_ID_PARAM) {
                type = NavType.StringType
            })
        ) {
            VenueDetailScreen()
        }
    }
}