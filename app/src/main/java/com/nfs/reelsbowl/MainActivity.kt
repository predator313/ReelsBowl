package com.nfs.reelsbowl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nfs.reelsbowl.login.presentation.AuthViewModel
import com.nfs.reelsbowl.ui.navigation.ReelsBowlNavHost
import com.nfs.reelsbowl.ui.theme.ReelsBowlTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ReelsBowlTheme {
                // Firebase restores the saved user synchronously, so this is correct from the
                // very first frame — no loading state needed.
                val user by authViewModel.user.collectAsStateWithLifecycle()
                ReelsBowlNavHost(
                    isSignedIn = user != null,
                    onOpenTerms = { /* TODO: open Terms of Service */ },
                    onOpenPrivacy = { /* TODO: open Privacy Policy */ },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}