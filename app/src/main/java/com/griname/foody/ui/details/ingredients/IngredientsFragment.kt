package com.griname.foody.ui.details.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.griname.foody.R
import com.griname.foody.data.network.model.Result
import com.griname.foody.databinding.FragmentIngredientsBinding
import com.griname.foody.util.Constant.Companion.BUNDLE_RECIPE_RESULT_KEY

class IngredientsFragment : Fragment(R.layout.fragment_ingredients) {

    private val binding by viewBinding<FragmentIngredientsBinding>(CreateMethod.INFLATE)
    private val ingredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Result? = arguments?.getParcelable(BUNDLE_RECIPE_RESULT_KEY)

        binding.ingredientsRecyclerView.adapter = ingredientsAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        bundle?.extendedIngredient?.let { ingredientsAdapter.setData(it) }
    }
}