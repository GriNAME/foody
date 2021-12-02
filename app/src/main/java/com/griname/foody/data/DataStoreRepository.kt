package com.griname.foody.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.griname.foody.util.Constant.Companion.DEFAULT_DIET_TYPE
import com.griname.foody.util.Constant.Companion.DEFAULT_MEAL_TYPE
import com.griname.foody.util.Constant.Companion.DEFAULT_TYPE_ID
import com.griname.foody.util.Constant.Companion.PREFERENCES_DIET_TYPE
import com.griname.foody.util.Constant.Companion.PREFERENCES_DIET_TYPE_ID
import com.griname.foody.util.Constant.Companion.PREFERENCES_MEAL_TYPE
import com.griname.foody.util.Constant.Companion.PREFERENCES_MEAL_TYPE_ID
import com.griname.foody.util.Constant.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKey {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { preferences ->
            preferences[PreferenceKey.selectedMealType] = mealType
            preferences[PreferenceKey.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKey.selectedDietType] = dietType
            preferences[PreferenceKey.selectedDietTypeId] = dietTypeId
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val mealType = preferences[PreferenceKey.selectedMealType] ?: DEFAULT_MEAL_TYPE
                val mealTypeId = preferences[PreferenceKey.selectedMealTypeId] ?: DEFAULT_TYPE_ID
                val dietType = preferences[PreferenceKey.selectedDietType] ?: DEFAULT_DIET_TYPE
                val dietTypeId = preferences[PreferenceKey.selectedDietTypeId] ?: DEFAULT_TYPE_ID
                MealAndDietType(
                    mealType,
                    mealTypeId,
                    dietType,
                    dietTypeId
                )
            }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)