package uk.co.applylogic.marvel.app.application

import androidx.multidex.MultiDexApplication
import uk.co.applylogic.marvel.core_injection.CoreComponent
import uk.co.applylogic.marvel.core_injection.CoreComponentProvider
import uk.co.applylogic.marvel.core_injection.DaggerCoreComponent
import uk.co.applylogic.marvel.data.di.module.ApplicationModule
import uk.co.applylogic.marvel.data.di.module.NetworkModule

class MyApplication  : MultiDexApplication(), CoreComponentProvider {
    private lateinit var coreComponent: CoreComponent

    override fun provideCoreComponent(): CoreComponent {

        if (!this::coreComponent.isInitialized) {
            coreComponent = DaggerCoreComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .networkModule(NetworkModule(this))
                .build()
        }
        return coreComponent
    }
}