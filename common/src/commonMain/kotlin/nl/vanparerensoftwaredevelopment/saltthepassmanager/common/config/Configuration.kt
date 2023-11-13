package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.config

import kotlinx.serialization.Serializable
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.Platform

/**
 * The app's configuration options.
 *
 * This contains configuration options for all platforms.
 */
@Serializable
data class Configuration(
    val theme: Theme = Theme.default,
    val density: DensityLevel = DensityLevel.NORMAL,
    val enableTrayIcon: Boolean = false,
    val closeToTray: Boolean = false,
    val trayIconColor: TrayIconColor = TrayIconColor.default,
    val keepMasterPasswordOnClearForm: Boolean = false
) {
    /**
     * The configurable theme of the app.
     * In some environments, [SYSTEM] is not available.
     */
    @Serializable
    enum class Theme {
        LIGHT, DARK, SYSTEM;

        companion object {
            val default = when {
                Platform.isAndroid -> SYSTEM
                else -> LIGHT
            }
        }
    }

    /**
     * The color of the system tray icon.
     */
    @Serializable
    enum class TrayIconColor {
        LIGHT, DARK;

        companion object {
            val default = DARK
        }
    }

    /**
     * The user-configured density level of the UI.
     * This may be adjusted by the user for readability reasons,
     * to minimize its needed size on the screen (on desktop or
     * Android systems with floating window options) or for any
     * other reason, really.
     */
    enum class DensityLevel(
        val densityMultiplier: Float,
        val fontMultiplier: Float
    ) {
        TINY(0.8f, 0.85f),
        SMALL(0.9f, 1f),
        NORMAL(1f, 1f),
        LARGE(1.2f, 1.2f);

        fun next(): DensityLevel {
            val values = DensityLevel.values()
            return values[(ordinal + 1) % values.size]
        }
    }

    companion object {
        /**
         * The default configuration to be used for new items.
         */
        val DEFAULT = Configuration()
    }
}
