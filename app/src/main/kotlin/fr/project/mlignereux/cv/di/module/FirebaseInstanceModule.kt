package fr.project.mlignereux.cv.di.module

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseInstanceModule {

    @Provides
    @Singleton
    internal fun provideFirebaseDatabaseInstance(): FirebaseDatabase =
            FirebaseDatabase.getInstance().apply { setPersistenceEnabled(true) }

    @Provides
    @Singleton
    internal fun provideFirebase() : DatabaseReference = FirebaseDatabase.getInstance().reference

}