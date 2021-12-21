package com.griname.foody.ui.main.fragment.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.griname.foody.R
import com.griname.foody.databinding.FragmentFavoriteRecipesBinding
import com.griname.foody.ui.main.fragment.recipes.adapter.RecipeAdapter
import com.griname.foody.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {

    private val binding: FragmentFavoriteRecipesBinding by viewBinding(CreateMethod.INFLATE)
    private val favoriteAdapter by lazy { FavoriteRecipesAdapter() }
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) {
            if (it.isEmpty())
                visibleNotice()
            else
                invisibleNotice()
        }
    }

    private fun initRecyclerView() {
        binding.favoriteRecyclerView.adapter = favoriteAdapter
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) {
            favoriteAdapter.setData(it)
        }
    }

    private fun visibleNotice() {
        binding.noDataImage.visibility = View.VISIBLE
        binding.noDataText.visibility = View.VISIBLE
    }

    private fun invisibleNotice() {
        binding.noDataImage.visibility = View.INVISIBLE
        binding.noDataText.visibility = View.INVISIBLE
    }

}