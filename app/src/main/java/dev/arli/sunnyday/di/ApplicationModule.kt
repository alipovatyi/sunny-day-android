package dev.arli.sunnyday.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        ApplicationConfigModule::class
    ]
)
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule
