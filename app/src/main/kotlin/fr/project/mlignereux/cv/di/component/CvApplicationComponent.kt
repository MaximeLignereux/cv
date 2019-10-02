package fr.project.mlignereux.cv.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import fr.project.mlignereux.base.di.component.ApplicationComponent
import fr.project.mlignereux.cv.CvApplication
import fr.project.mlignereux.cv.di.module.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CvApplicationModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ImageLoaderModule::class,
        FirebaseInstanceModule::class,
        AndroidSupportInjectionModule::class]
)
interface CvApplicationComponent  : ApplicationComponent, AndroidInjector<CvApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: CvApplication): Builder

        fun build(): CvApplicationComponent
    }
}