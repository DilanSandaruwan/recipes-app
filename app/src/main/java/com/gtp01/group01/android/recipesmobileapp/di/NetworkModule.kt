package com.gtp01.group01.android.recipesmobileapp.di

import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides the base URL of the Rest API.
     *
     * @return Base URL as a String.
     */
    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return ConstantNetworkService.BASE_URL.value
    }

    /**
     * Provides a Gson Converter Factory for JSON serialization and deserialization.
     *
     * @return Converter.Factory instance for Gson-based conversion.
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    /**
     * Provides the HTTP client for making network requests.
     */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Provides a Retrofit instance for making network requests.
     *
     * @param baseUrl Base URL of the API.
     * @param okHttpClient OkHttpClient with interceptors.
     * @param converterFactory Converter Factory for JSON conversion.
     *
     * @return Retrofit instance configured for the given parameters.
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }
}