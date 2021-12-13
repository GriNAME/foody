package com.griname.foody.ui.main.fragment.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.griname.foody.databinding.RecipeBottomSheetBinding
import com.griname.foody.util.Constant.Companion.DEFAULT_DIET_TYPE
import com.griname.foody.util.Constant.Companion.DEFAULT_MEAL_TYPE
import com.griname.foody.util.Constant.Companion.DEFAULT_TYPE_ID
import com.griname.foody.ui.main.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.*

@AndroidEntryPoint
class RecipeBottomSheet : BottomSheetDialogFragment() {

    private val TAG = "RecipeBottomSheetLog"
    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val recipeViewModel by viewModels<RecipeViewModel>()

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = DEFAULT_TYPE_ID
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = DEFAULT_TYPE_ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = RecipeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) {
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType

            updateChip(it.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(it.selectedDietTypeId, binding.dietTypeChipGroup)
        }

        binding.apply {

            mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
                val chip = group.findViewById<Chip>(checkedId)
                val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
                mealTypeChip = selectedMealType
                mealTypeChipId = checkedId
            }

            dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
                val chip = group.findViewById<Chip>(checkedId)
                val selectedDiet = chip.text.toString().lowercase(Locale.ROOT)
                dietTypeChip = selectedDiet
                dietTypeChipId = checkedId
            }

            applyButton.setOnClickListener {
                recipeViewModel.saveMealAndDietType(
                    mealTypeChip,
                    mealTypeChipId,
                    dietTypeChip,
                    dietTypeChipId
                )
                val action = RecipeBottomSheetDirections.actionRecipeBottomSheetToRecipesFragment(true)
                findNavController().navigate(action)
            }
        }
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d(TAG, "updateChip: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}