package fr.project.mlignereux.base.di.component

import dagger.Component
import fr.project.mlignereux.base.di.module.ApplicationModule
import fr.project.mlignereux.base.di.module.BaseModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class,
        BaseModule::class]
)
interface ApplicationComponent