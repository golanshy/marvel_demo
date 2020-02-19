package uk.co.applylogic.marvel.core_injection

import android.app.Application
import android.content.Context
import uk.co.applylogic.marvel.data.di.qualifier.ApplicationContext
import dagger.Component
import uk.co.applylogic.marvel.core_android.handler.NetworkErrorHandlerInterface
import uk.co.applylogic.marvel.data.di.module.ApplicationModule
import uk.co.applylogic.marvel.data.di.module.NetworkModule
import uk.co.applylogic.marvel.data.repository.ContentInterface
import uk.co.applylogic.marvel.data.repository.ContentRepositoryContract
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class,
	NetworkModule::class],
		dependencies = [])
interface CoreComponent {

	@ApplicationContext
	fun context(): Context

	fun application(): Application

	fun contentRepository(): ContentRepositoryContract

	fun contentInterface(): ContentInterface

	fun networkErrorHandler(): NetworkErrorHandlerInterface
}
