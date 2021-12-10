package com.griname.foody.ui.details.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.griname.foody.R
import com.griname.foody.databinding.FragmentIngredientsBinding

class IngredientsFragment : Fragment(R.layout.fragment_ingredients) {

    private val binding by viewBinding<FragmentIngredientsBinding>(CreateMethod.INFLATE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        binding.root
}