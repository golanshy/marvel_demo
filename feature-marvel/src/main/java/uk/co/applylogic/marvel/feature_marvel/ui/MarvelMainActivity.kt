package uk.co.applylogic.marvel.feature_marvel.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uk.co.applylogic.marvel.core_injection.CoreInjectHelper
import uk.co.applylogic.marvel.feature_marvel.R
import uk.co.applylogic.marvel.feature_marvel.di.ContentComponent
import uk.co.applylogic.marvel.feature_marvel.di.DaggerContentComponent

class MarvelMainActivity : AppCompatActivity() {

    internal lateinit var comp: ContentComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        comp = DaggerContentComponent
            .builder()
            .coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
            .build()

        comp.injectActivity(this)
    }
}
