package fr.project.mlignereux.cv

import com.google.firebase.database.FirebaseDatabase
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import fr.project.mlignereux.base.BaseApplication
import fr.project.mlignereux.cv.di.component.CvApplicationComponent
import fr.project.mlignereux.cv.di.component.DaggerCvApplicationComponent

class CvApplication : BaseApplication<CvApplicationComponent>() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerCvApplicationComponent
            .builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

    }

    companion object {

        private val TAG = "CvApplication"
    }
}
