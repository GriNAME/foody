package com.griname.foody.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.griname.foody.data.network.model.FoodRecipe
import com.griname.foody.util.Constant

@Entity(tableName = Constant.RECIPE_TABLE)
class RecipeEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}