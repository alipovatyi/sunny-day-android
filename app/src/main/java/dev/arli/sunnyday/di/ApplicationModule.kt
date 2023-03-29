@file:Suppress("UnnecessaryAbstractClass")

package dev.arli.sunnyday.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.arli.sunnyday.data.api.retrofit.di.RetrofitApiModule

@Module(
    includes = [
        ApplicationConfigModule::class,
        RetrofitApiModule::class
    ]
)
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule
