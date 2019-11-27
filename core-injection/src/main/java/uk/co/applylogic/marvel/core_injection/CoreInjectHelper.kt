package uk.co.applylogic.marvel.core_injection

import android.content.Context

object CoreInjectHelper {
    fun provideCoreComponent(applicationContext: Context): CoreComponent {
        return if (applicationContext is CoreComponentProvider) {
            (applicationContext as CoreComponentProvider).provideCoreComponent()
        } else {
            throw IllegalStateException(
                    "The application context you have passed does not implement CoreComponentProvider")
        }
    }
}
