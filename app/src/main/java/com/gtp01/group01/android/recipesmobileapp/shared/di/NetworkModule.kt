package com.gtp01.group01.android.recipesmobileapp.shared.di

import android.util.Log
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.AuthRepository
import com.gtp01.group01.android.recipesmobileapp.shared.sources.AuthApiService
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
        return ConstantNetworkService.BASE_URL
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
    /**
     * Provides an instance of the [AuthApiService] using the provided [Retrofit] instance.
     *
     * @param retrofit The Retrofit instance used for creating the [AuthApiService].
     * @return An instance of [AuthApiService] for making API requests related to user authentication.
     */
    @Singleton
    @Provides
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
    /**
     * Provides an instance of [AuthRepository] using the provided [AuthApiService].
     *
     * @param authApiService The [AuthApiService] instance used for creating the [AuthRepository].
     * @return An instance of [AuthRepository] for handling user authentication-related operations.
     * @throws Exception if an error occurs during the creation of [AuthRepository].
     */
    @Singleton
    @Provides
    fun provideAuthRepository(authApiService: AuthApiService): AuthRepository {
        try {
            val authRepo = AuthRepository(authApiService)
            logMessage( "Repository provided")
            return authRepo
        } catch (e: Exception) {
            logMessage( "NetworkError while providing bRepository: ${e.message}")
            throw e  // Re-throw the exception for higher-level error handling
        } }
    /**
     * Logs a message to indicate the status or action taken within the network module.
     *
     * @param message The message to be logged.
     */
        private fun logMessage(message: String) {
            Log.d("NetworkModule", message)
        }
}