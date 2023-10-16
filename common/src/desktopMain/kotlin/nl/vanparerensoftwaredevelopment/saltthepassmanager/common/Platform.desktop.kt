package nl.vanparerensoftwaredevelopment.saltthepassmanager.common

import androidx.compose.runtime.Composable
import mu.KotlinLogging
import java.awt.Desktop
import java.net.URI
import java.util.*

private val logger = KotlinLogging.logger {}

/**
 * Platform info, made available in a way that doesn't
 * break the non-desktop builds.
 */
actual object Platform {
    actual val isDesktop = true
    actual val isAndroid = false
    actual val isTraySupported
        get() = androidx.compose.ui.window.isTraySupported

    @Composable
    actual fun getOpenUrlInBrowserMethod(): (url: String) -> Unit = Platform::openUrlInBrowser

    private fun openUrlInBrowser(url: String) {
        /*
         * This is a best-effort attempt at opening the browser on desktop platforms.
         * It has been adapted from https://stackoverflow.com/a/68426773
         */
        val osName = System.getProperty("os.name").lowercase(Locale.US)
        val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
        when {
            desktop != null && desktop.isSupported(Desktop.Action.BROWSE) -> desktop.browse(URI.create(url))
            "mac" in osName -> Runtime.getRuntime().exec("open $url")
            "nix" in osName || "linux" in osName -> Runtime.getRuntime().exec("xdg-open $url")
            else -> {
                // Don't crash the system, but do log/print the problem
                logger.warn {
                    val details = buildString {
                        append("desktop ")
                        append(if (desktop == null) "==" else "!==")
                        append(" null,")
                        if (desktop != null) {
                            append(" BROWSE supported == ")
                            append(desktop.isSupported(Desktop.Action.BROWSE))
                            append(",")
                        }
                        append(" OS name = \"")
                        append(osName)
                        append("\"")
                    }
                    "Cannot open URL in browser: operation not supported on the current system ($details)"
                }
            }
        }
    }
}