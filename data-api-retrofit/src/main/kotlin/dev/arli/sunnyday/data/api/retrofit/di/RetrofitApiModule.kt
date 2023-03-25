package dev.arli.sunnyday.data.api.retrofit.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dev.arli.sunnyday.data.api.retrofit.adapter.EitherCallAdapterFactory
import dev.arli.sunnyday.data.api.retrofit.service.RetrofitWeatherService
import dev.arli.sunnyday.data.api.service.WeatherService
import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

@OptIn(ExperimentalSerializationApi::class)
@Module
object RetrofitApiModule {

    @Singleton
    @Provides
    internal fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        configDataSource: ConfigDataSource,
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = MediaType.parse("application/json")!!
        return Retrofit.Builder()
            .baseUrl(configDataSource.apiUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService = retrofit.create<RetrofitWeatherService>()
}
