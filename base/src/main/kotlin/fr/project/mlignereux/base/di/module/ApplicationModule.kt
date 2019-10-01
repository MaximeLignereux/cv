package fr.project.mlignereux.base.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import fr.project.mlignereux.base.di.qualifier.ApplicationContext

@Module
class ApplicationModule {

    @Provides
    @ApplicationContext
    internal fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    internal fun provideApplication(application: Application): Application {
        return application
    }
}