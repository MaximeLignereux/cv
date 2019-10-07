package fr.project.mlignereux.cv.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fr.project.mlignereux.base.di.scope.PerFragment
import fr.project.mlignereux.cv.view.fragment.*

@Module
abstract class FragmentModule {

    @PerFragment
    @ContributesAndroidInjector
    abstract fun contactFragment(): ContactFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun dataFragment(): DataFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun profilFragment(): ProfilFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun skillFragment(): SkillFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun privacyPolicyFragment(): PrivacyPolicyFragment
}