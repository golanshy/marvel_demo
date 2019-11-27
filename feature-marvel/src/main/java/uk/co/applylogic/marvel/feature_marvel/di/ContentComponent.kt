package uk.co.applylogic.marvel.feature_marvel.di

import android.app.Application
import android.content.Context
import com.openpayd.core.android.di.qualifier.ApplicationContext
import com.openpayd.core.android.di.scope.ActivityScope
import dagger.Component
import uk.co.applylogic.marvel.core_android.handler.NetworkErrorHandlerInterface
import uk.co.applylogic.marvel.core_injection.CoreComponent
import uk.co.applylogic.marvel.data.repository.ContentInterface
import uk.co.applylogic.marvel.data.repository.ContentRepository
import uk.co.applylogic.marvel.data.repository.ContentRepositoryContract
import uk.co.applylogic.marvel.feature_marvel.ui.MarvelMainActivity
import uk.co.applylogic.marvel.feature_marvel.ui.MarvelMainFragment
import uk.co.applylogic.marvel.feature_marvel.ui.MarvelMainViewModel

@ActivityScope
@Component(
        dependencies = [CoreComponent::class],
        modules = [ContentModule::class]
)
interface ContentComponent {

    @ApplicationContext
    fun context(): Context
    fun application(): Application

    fun networkErrorHandler(): NetworkErrorHandlerInterface
    fun contentInterface(): ContentInterface
    fun contentRepository(): ContentRepositoryContract

    fun injectViewModel(model: MarvelMainViewModel)
    fun injectActivity(activity: MarvelMainActivity)
    fun injectFragment(fragment: MarvelMainFragment)
}
