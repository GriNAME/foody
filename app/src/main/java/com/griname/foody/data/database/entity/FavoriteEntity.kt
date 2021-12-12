package com.griname.foody.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.griname.foody.data.network.model.Result
import com.griname.foody.util.Constant.Companion.FAVORITE_RECIPE_TABLE

@Entity(tableName = FAVORITE_RECIPE_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var result: Result
)