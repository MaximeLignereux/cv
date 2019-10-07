package fr.project.mlignereux.base.di.module

import dagger.Module
import dagger.Provides
import fr.project.mlignereux.base.util.exception.CrashlyticsExceptionLogger
import fr.project.mlignereux.base.util.exception.ExceptionLogger
import javax.inject.Singleton

@Module
class CrashlyticsModule {

    @Provides
    @Singleton
    internal fun provideExceptionLogger(): ExceptionLogger = CrashlyticsExceptionLogger()
}