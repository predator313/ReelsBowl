package com.nfs.reelsbowl.ui.navigation

import kotlinx.serialization.Serializable

/** Type-safe navigation routes. Serializable objects, not strings, so arguments stay typed. */
@Serializable
data object LoginRoute

@Serializable
data object HomeRoute
