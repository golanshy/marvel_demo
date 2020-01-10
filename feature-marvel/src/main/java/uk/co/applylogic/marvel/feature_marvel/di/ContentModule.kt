package uk.co.applylogic.marvel.feature_marvel.di

import uk.co.applylogic.marvel.data.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import uk.co.applylogic.marvel.feature_marvel.ui.MarvelMainActivity
import uk.co.applylogic.marvel.feature_marvel.ui.MarvelMainViewModel

@Module
class ContentModule {

    @Provides
    @ActivityScope
    fun provideMarvelMainActivity(activity: MarvelMainActivity): MarvelMainActivity {
        return activity
    }

    @Provides
    @ActivityScope
    fun provideMarvelMainViewModel(viewModel: MarvelMainViewModel): MarvelMainViewModel {
        return viewModel
    }
}
