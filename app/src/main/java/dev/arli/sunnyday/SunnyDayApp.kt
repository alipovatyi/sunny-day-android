package dev.arli.sunnyday

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp
import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import javax.inject.Inject

@HiltAndroidApp
class SunnyDayApp : Application() {

    @Inject
    lateinit var configDataSource: ConfigDataSource

    override fun onCreate() {
        super.onCreate()
        initPlaces()
    }

    private fun initPlaces() {
        Places.initialize(applicationContext, configDataSource.googleMapsApiKey)
    }
}
