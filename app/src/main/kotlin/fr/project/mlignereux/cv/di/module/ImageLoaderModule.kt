package fr.project.mlignereux.cv.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.project.mlignereux.cv.view.ImageLoader
import javax.inject.Singleton

@Module
class ImageLoaderModule {

    @Provides
    @Singleton
    internal fun provideImageLoader(context: Context): ImageLoader = ImageLoader.Implementation(context)
}