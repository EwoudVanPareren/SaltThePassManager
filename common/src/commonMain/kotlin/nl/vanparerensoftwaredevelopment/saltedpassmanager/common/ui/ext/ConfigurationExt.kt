package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.ext

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanager.resources.MR

/**
 * Get the user-facing display name for the theme.
 */
@Composable
fun Configuration.Theme.displayName(): String = stringResource {
    when (this@displayName) {
        Configuration.Theme.LIGHT -> theme_light_label
        Configuration.Theme.DARK -> theme_dark_label
        Configuration.Theme.SYSTEM -> theme_system_label
    }
}

/**
 * Get the user-facing display name for the tray icon color.
 */
@Composable
fun Configuration.TrayIconColor.displayName(): String = stringResource {
    when (this@displayName) {
        Configuration.TrayIconColor.LIGHT -> tray_icon_color_light_label
        Configuration.TrayIconColor.DARK -> tray_icon_color_dark_label
    }
}

/**
 * Get the user-facing display name for the density level.
 */
@Composable
fun Configuration.DensityLevel.displayName(): String = stringResource {
    when (this@displayName) {
        Configuration.DensityLevel.TINY -> density_level_tiny_label
        Configuration.DensityLevel.SMALL -> density_level_small_label
        Configuration.DensityLevel.NORMAL -> density_level_normal_label
        Configuration.DensityLevel.LARGE -> density_level_large_label
    }
}

/**
 * Convenience method to avoid having a lot of "`MR.strings.`" duplicated around
 * this file.
 */
@Composable
private inline fun stringResource(getResource: MR.strings.() -> StringResource): String =
    dev.icerock.moko.resources.compose.stringResource(MR.strings.getResource())
