package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.components

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.resources.MR

@Composable
fun Configuration.Theme.displayName(): String = stringResource {
    when (this@displayName) {
        Configuration.Theme.LIGHT -> theme_light_label
        Configuration.Theme.DARK -> theme_dark_label
        Configuration.Theme.SYSTEM -> theme_system_label
    }
}

@Composable
fun Configuration.DensityLevel.displayName(): String = stringResource {
    when (this@displayName) {
        Configuration.DensityLevel.TINY -> density_level_tiny_label
        Configuration.DensityLevel.SMALL -> density_level_small_label
        Configuration.DensityLevel.NORMAL -> density_level_normal_label
        Configuration.DensityLevel.LARGE -> density_level_large_label
    }
}

@Composable
private inline fun stringResource(getResource: MR.strings.() -> StringResource): String =
    dev.icerock.moko.resources.compose.stringResource(MR.strings.getResource())