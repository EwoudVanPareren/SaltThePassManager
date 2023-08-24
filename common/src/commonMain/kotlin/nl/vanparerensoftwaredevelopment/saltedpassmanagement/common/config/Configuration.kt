package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.config

import kotlinx.serialization.Serializable
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.Platform

/**
 * The app's configuration options.
 */
@Serializable
data class Configuration(
    val theme: Theme = Theme.default,
    val density: DensityLevel = DensityLevel.NORMAL
) {
    @Serializable
    enum class Theme {
        LIGHT, DARK, SYSTEM;

        fun next(): Theme {
            val values = Theme.values()
            return values[(ordinal + 1) % values.size]
        }

        companion object {
            val default = when {
                Platform.isAndroid -> Configuration.Theme.SYSTEM
                else -> Configuration.Theme.LIGHT
            }
        }
    }

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
}
