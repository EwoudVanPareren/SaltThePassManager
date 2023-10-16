package nl.vanparerensoftwaredevelopment.saltedpassmanager.android

import android.app.Application
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.di.commonModule
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.di.androidModule
import org.kodein.di.DI
import org.kodein.di.android.x.androidXModule

/**
 * The application class for SaltThePassManager.
 *
 * This allows access to the KodeIn DI instance.
 */
class SaltThePassManagerApplication: Application() {
    lateinit var di: DI

    override fun onCreate() {
        super.onCreate()

        di = DI {
            import(androidXModule(this@SaltThePassManagerApplication))
            import(commonModule)
            import(androidModule)
        }
    }
}