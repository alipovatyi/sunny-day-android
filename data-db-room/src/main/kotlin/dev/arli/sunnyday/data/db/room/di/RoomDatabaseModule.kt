package dev.arli.sunnyday.data.db.room.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.arli.sunnyday.data.db.DatabaseTransactionRunner
import dev.arli.sunnyday.data.db.dao.CurrentWeatherDao
import dev.arli.sunnyday.data.db.dao.LocationDao
import dev.arli.sunnyday.data.db.room.RoomDatabaseTransactionRunner
import dev.arli.sunnyday.data.db.room.SunnyDayDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomDatabaseModule {

    private const val DatabaseName = "sunny_day.db"

    @Singleton
    @Provides
    fun provideSunnyDayRoomDatabase(
        @ApplicationContext context: Context
    ): SunnyDayDatabase {
        return Room.databaseBuilder(context, SunnyDayDatabase::class.java, DatabaseName)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideLocationDao(database: SunnyDayDatabase): LocationDao = database.locationDao()

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(database: SunnyDayDatabase): CurrentWeatherDao = database.currentWeatherDao()

    @Singleton
    @Provides
    fun provideRoomDatabaseTransactionRunner(
        roomDatabaseTransactionRunner: RoomDatabaseTransactionRunner
    ): DatabaseTransactionRunner = roomDatabaseTransactionRunner
}
