package fr.project.mlignereux.base.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fr.project.mlignereux.base.di.scope.PerActivity
import fr.project.mlignereux.base.ui.base.BaseActivity

@Module
abstract class BaseModule {

    @PerActivity
    @ContributesAndroidInjector
    abstract fun bindBaseActivity(): BaseActivity

}