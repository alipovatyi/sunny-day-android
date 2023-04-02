@file:Suppress("UnnecessaryAbstractClass")

package dev.arli.sunnyday.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import dev.arli.sunnyday.datasource.ApplicationConfigDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ApplicationConfigModule {

    @Singleton
    @Binds
    abstract fun bindApplicationConfigDataSource(
        applicationConfigDataSource: ApplicationConfigDataSource
    ): ConfigDataSource
}
