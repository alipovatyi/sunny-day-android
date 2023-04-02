@file:Suppress("UnnecessaryAbstractClass")

package dev.arli.sunnyday.data.device.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.arli.sunnyday.data.common.datasource.DateTimeDataSource
import dev.arli.sunnyday.data.device.datasource.DeviceDateTimeDataSource
import dev.arli.sunnyday.data.device.datasource.DeviceLocationDataSourceImpl
import dev.arli.sunnyday.data.location.datasource.DeviceLocationDataSource
import java.util.Locale

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DeviceDataModule {

    @Binds
    abstract fun bindDeviceLocationDataSource(
        deviceLocationDataSourceImpl: DeviceLocationDataSourceImpl
    ): DeviceLocationDataSource

    @Binds
    abstract fun bindDeviceDateTimeDataSource(
        deviceDateTimeDataSource: DeviceDateTimeDataSource
    ): DateTimeDataSource

    companion object {

        @Provides
        fun provideFusedLocationProviderClient(
            @ApplicationContext context: Context
        ): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(context)
        }

        @Provides
        fun provideGeocoder(
            @ApplicationContext context: Context
        ): Geocoder {
            return Geocoder(context, Locale.getDefault())
        }
    }
}
