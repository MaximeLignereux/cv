package fr.project.mlignereux.cv.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fr.project.mlignereux.base.di.scope.PerActivity
import fr.project.mlignereux.cv.view.activity.MainActivity

@Module
abstract class ActivityModule {

    @PerActivity
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}