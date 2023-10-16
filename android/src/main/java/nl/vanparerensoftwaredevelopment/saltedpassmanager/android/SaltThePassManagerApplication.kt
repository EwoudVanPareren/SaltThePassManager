package nl.vanparerensoftwaredevelopment.saltedpassmanager.android

import android.app.Application
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.di.commonModule
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.model.UserFiles
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.ext.from
import org.kodein.di.DI
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindSingleton
import org.kodein.di.instance

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

            bindSingleton<UserFiles> {
                UserFiles.from(instance())
            }
        }
    }
}