package com.gtp01.group01.android.recipesmobileapp.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
/**
 * Dagger Hilt module responsible for providing SharedPreferences instance.
 */
@Module
@InstallIn(SingletonComponent::class)
object CommonDataModule {
    /**
     * Provides a SharedPreferences instance for the application.
     *
     * @param context The application context.
     * @return SharedPreferences instance.
     */
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("RecipeApp", Context.MODE_PRIVATE)
    }
}