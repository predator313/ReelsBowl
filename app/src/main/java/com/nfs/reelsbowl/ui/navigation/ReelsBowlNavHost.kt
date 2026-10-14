package com.nfs.reelsbowl.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nfs.reelsbowl.login.presentation.LoginRoute as LoginScreenRoute
import com.nfs.reelsbowl.ui.home.HomeRoute as HomeScreenRoute

@Composable
fun ReelsBowlNavHost(
    isSignedIn: Boolean,
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    // Runs only when isSignedIn flips: sign-in → Home, sign-out → Login.
    // (A guest on Home is unaffected, since isSignedIn never changed for them.)
    LaunchedEffect(isSignedIn) {
        navController.navigateTopLevel(if (isSignedIn) HomeRoute else LoginRoute)
    }

    NavHost(
        navController = navController,
        startDestination = if (isSignedIn) HomeRoute else LoginRoute,
        modifier = modifier,
    ) {
        composable<LoginRoute> {
            LoginScreenRoute(
                onOpenTerms = onOpenTerms,
                onOpenPrivacy = onOpenPrivacy,
                onContinueAsGuest = { navController.navigateTopLevel(HomeRoute) },
            )
        }
        composable<HomeRoute> {
            HomeScreenRoute()
        }
    }
}

/** Replaces the whole back stack — used for session changes, where "back" makes no sense. */
private fun NavController.navigateTopLevel(route: Any) {
    // Already there: the effect also runs on first composition, when startDestination matches.
    if (currentDestination?.hasRoute(route::class) == true) return
    navigate(route) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}
