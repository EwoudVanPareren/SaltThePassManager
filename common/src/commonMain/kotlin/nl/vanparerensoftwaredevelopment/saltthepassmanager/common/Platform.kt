package nl.vanparerensoftwaredevelopment.saltthepassmanager.common

import androidx.compose.runtime.Composable

/**
 * Platform info, made available in a way that doesn't
 * break the non-desktop builds.
 */
expect object Platform {
    val isDesktop: Boolean
    val isAndroid: Boolean
    val isTraySupported: Boolean
    @Composable
    fun getOpenUrlInBrowserMethod(): (url: String) -> Unit
}