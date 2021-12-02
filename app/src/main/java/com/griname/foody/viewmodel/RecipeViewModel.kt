package com.griname.foody.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.griname.foody.data.DataStoreRepository
import com.griname.foody.util.Constant.Companion.API_KEY
import com.griname.foody.util.Constant.Companion.DEFAULT_DIET_TYPE
import com.griname.foody.util.Constant.Companion.DEFAULT_MEAL_TYPE
import com.griname.foody.util.Constant.Companion.DEFAULT_RECIPE_NUMBER
import com.griname.foody.util.Constant.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.griname.foody.util.Constant.Companion.QUERY_API_KEY
import com.griname.foody.util.Constant.Companion.QUERY_DIET
import com.griname.foody.util.Constant.Companion.QUERY_FILL_INGREDIENTS
import com.griname.foody.util.Constant.Companion.QUERY_NUMBER
import com.griname.foody.util.Constant.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect {
                mealType = it.selectedMealType
                dietType = it.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}