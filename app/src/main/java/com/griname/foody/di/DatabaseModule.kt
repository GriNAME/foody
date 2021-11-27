package com.griname.foody.di

import android.content.Context
import androidx.room.Room
import com.griname.foody.data.database.RecipeDao
import com.griname.foody.data.database.RecipeDatabase
import com.griname.foody.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RecipeDatabase = Room.databaseBuilder(
        context,
        RecipeDatabase::class.java,
        Constant.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: RecipeDatabase): RecipeDao = database.getRecipeDao()
}