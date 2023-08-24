import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.awaitApplication
import androidx.compose.ui.window.rememberWindowState
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.App
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.di.commonModule
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.AppIcons
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.windowsize.withLocalWindowSize
import org.kodein.di.DI
import org.kodein.di.bindInstance
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance

fun main() = runBlocking {
    val di = DI {
        import(commonModule)
        bindInstance<CoroutineScope>(tag = "appScope") {
            this@runBlocking
        }
    }

    awaitApplication {
        withDI(di) {

            val store: KStore<Configuration> by localDI().instance()
            val density by store.updates.map { it!!.density }.collectAsState(Configuration.DensityLevel.NORMAL)
            // TODO: Move density stuff to separate place
            val currentDensity = LocalDensity.current
            val newDensity = Density(
                density = currentDensity.density * density.densityMultiplier,
                fontScale = currentDensity.fontScale * (density.fontMultiplier / density.densityMultiplier)
            )

                val windowState = rememberWindowState()
                Window(
                    title = "SaltThePass Manager",
                    onCloseRequest = ::exitApplication,
                    icon = rememberVectorPainter(AppIcons.Lock),
                    state = windowState
                ) {
                    CompositionLocalProvider(values = arrayOf(LocalDensity.provides(newDensity))) {
                        withLocalWindowSize(windowState.size / density.densityMultiplier) {
                            App()
                        }
                    }
                }
        }
    }
}
