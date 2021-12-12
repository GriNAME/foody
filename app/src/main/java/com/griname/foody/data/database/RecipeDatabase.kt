package com.griname.foody.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.griname.foody.data.database.entity.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(RecipeTypeConverter::class)
abstract class RecipeDatabase: RoomDatabase() {

    abstract fun getRecipeDao(): RecipeDao
}