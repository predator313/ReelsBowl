package com.nfs.reelsbowl.login.domain

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class AuthUiState(
    val isSigningIn: Boolean = false,
    @param:StringRes val errorMessage: Int? = null,
)
