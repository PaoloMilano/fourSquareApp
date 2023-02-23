package com.magicbluepenguin.features.venuesearch

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.magicbluepenguin.features.venuesearch.detail.VENUE_ID_PARAM
import com.magicbluepenguin.features.venuesearch.detail.VenueDetailScreen
import com.magicbluepenguin.features.venuesearch.list.VenueSearchScreen

@Composable
fun VenueSearch() {

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