package fr.project.mlignereux.cv.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.project.mlignereux.base.di.module.CrashlyticsModule
import fr.project.mlignereux.cv.CvApplication

@Module(includes = [CrashlyticsModule::class])
class CvApplicationModule {

    @Provides
    fun provideContext(application: CvApplication): Context {
        return application.applicationContext
    }
}