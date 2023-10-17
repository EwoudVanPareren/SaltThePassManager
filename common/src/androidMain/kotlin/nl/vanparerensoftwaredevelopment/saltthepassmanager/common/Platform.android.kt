package nl.vanparerensoftwaredevelopment.saltthepassmanager.common

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import mu.KotlinLogging
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ext.getActivity

private val logger = KotlinLogging.logger { }

/**
 * Platform info, made available in a way that doesn't
 * break the non-desktop builds.
 */
actual object Platform {
    actual val isDesktop = false
    actual val isAndroid = true
    actual val isTraySupported = false

    @Composable
    actual fun getOpenUrlInBrowserMethod(): (url: String) -> Unit {
        val activity = LocalContext.current.getActivity()
        return when (activity) {
            null -> {{ url ->
                logger.warn {
                    "Failed to open $url: current activity not found"
                }
            }}
            else -> {{ url ->
                try {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: ActivityNotFoundException) {
                    logger.warn(e) {
                        "Failed to open $url: activity not found"
                    }
                }
            }}
        }
    }
}