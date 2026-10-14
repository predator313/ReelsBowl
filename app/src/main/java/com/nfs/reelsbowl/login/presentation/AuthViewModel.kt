package com.nfs.reelsbowl.login.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.nfs.reelsbowl.R
import com.nfs.reelsbowl.login.data.GoogleAuthManager
import com.nfs.reelsbowl.login.domain.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: GoogleAuthManager,
) : ViewModel() {

    val user: StateFlow<FirebaseUser?> = authManager.user

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signInWithGoogle(activity: Context) {
        if (_uiState.value.isSigningIn) return
        _uiState.update {
            it.copy(
                isSigningIn = true
            )
        }

        viewModelScope.launch {
            val error: Int? = try {
                authManager.signIn(activity)
                null
            } catch (e: Exception) {
                Timber.e(e, "Google sign-in failed ${e.localizedMessage}")
                R.string.auth_error_generic
            }
            _uiState.update {
                it.copy(errorMessage = error)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch { authManager.signOut() }
    }

    fun onErrorShown() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}
