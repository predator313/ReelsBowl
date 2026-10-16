package com.nfs.reelsbowl.login.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nfs.reelsbowl.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Everything about Google sign-in lives here.
 *
 * Sign-in is two steps:
 *  1. Credential Manager shows the Google account picker and gives us an **ID token**.
 *  2. We hand that token to Firebase, which verifies it and creates the session.
 *
 * Firebase saves the session on disk, so [user] is already filled in when the app restarts.
 */
@Singleton
class GoogleAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)

    private val _user = MutableStateFlow(firebaseAuth.currentUser)

    /** The signed-in user, or null when signed out. */
    val user: StateFlow<FirebaseUser?> = _user.asStateFlow()

    init {
        // Keeps [user] up to date on every sign-in / sign-out.
        firebaseAuth.addAuthStateListener { _user.value = it.currentUser }
    }

    suspend fun signIn(activity: Context) {
        // Step 1: ask Google for an ID token. The client ID must be the *Web* client ID,
        // which google-services.json generates as R.string.default_web_client_id.
        val googleOption = GetSignInWithGoogleOption
            .Builder(context.getString(R.string.default_web_client_id))
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()
        val response = credentialManager.getCredential(activity, request)
        val idToken = GoogleIdTokenCredential.createFrom(response.credential.data).idToken

        // Step 2: exchange the Google ID token for a Firebase session.
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential).await()
    }
    suspend fun signOut() {
        firebaseAuth.signOut()
        // Makes Google forget the last picked account, so "switch account" works next time.
        runCatching { credentialManager.clearCredentialState(ClearCredentialStateRequest()) }
    }
}
