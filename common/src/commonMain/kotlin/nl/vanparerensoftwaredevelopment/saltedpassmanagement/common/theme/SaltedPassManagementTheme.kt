package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.map
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.config.Configuration
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SaltedPassManagementTheme(content: @Composable () -> Unit) {
    val store: KStore<Configuration> by localDI().instance()
    val theme by store.updates.map { it!!.theme }.collectAsState(Configuration.Theme.SYSTEM)
    val darkTheme = when (theme) {
        Configuration.Theme.LIGHT -> false
        Configuration.Theme.DARK -> true
        Configuration.Theme.SYSTEM -> isSystemInDarkTheme()
    }

    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    ) {
        content()
    }
}