@file:Suppress("UnnecessaryAbstractClass")

package dev.arli.sunnyday.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.arli.sunnyday.navigator.AppNavigator
import dev.arli.sunnyday.ui.navigation.navigator.Navigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NavigatorModule {

    @Singleton
    @Binds
    abstract fun bindAppNavigator(
        appNavigator: AppNavigator
    ): Navigator
}
