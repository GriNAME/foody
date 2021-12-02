package com.griname.foody.ui.fragment.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.griname.foody.R
import com.griname.foody.viewmodel.MainViewModel
import com.griname.foody.adapter.RecipeAdapter
import com.griname.foody.databinding.FragmentRecipesBinding
import com.griname.foody.util.NetworkResult
import com.griname.foody.util.observeOnce
import com.griname.foody.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private val TAG = "RecipesFragment"
    private val binding by viewBinding<FragmentRecipesBinding>(CreateMethod.INFLATE)

    private val mainViewModel by viewModels<MainViewModel>()
    private val recipesViewModel by viewModels<RecipeViewModel>()
    private val mediatorLiveData = MediatorLiveData<Boolean>()

    private val recipesAdapter by lazy { RecipeAdapter() }
    private val args by navArgs<RecipesFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        checkingChangeListData()
        readDatabase()

        binding.recipesFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipeBottomSheet)
        }
    }

    private fun checkingChangeListData() {

        mediatorLiveData.addSource(mainViewModel.readRecipes) {
            if (!mainViewModel.hasInternetConnection())
                mediatorLiveData.value = it.isNullOrEmpty()
        }

        mediatorLiveData.addSource(mainViewModel.recipeResponse) {
            mediatorLiveData.value = true
            when (it) {
                is NetworkResult.Error -> {
                    mediatorLiveData.value = true
                    binding.errorText.text = it.message
                }
                is NetworkResult.Loading -> mediatorLiveData.value = false
                is NetworkResult.Success -> mediatorLiveData.value = false
            }
        }

        mediatorLiveData.observe(viewLifecycleOwner) {
            if (it) {
                visibleNotice()
            } else {
                invisibleNotice()
            }
        }
    }

    private fun initRecyclerView() {

        binding.shimmerRecyclerView.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun readDatabase() {

        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomShit) {
                    Log.d(TAG, "readDatabase: Local Data Showed")
                    recipesAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {

        Log.d(TAG, "requestApiData: Remote Data Showed")
        showShimmerEffect()
        mainViewModel.getRecipes(recipesViewModel.applyQueries())

        mainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data!!.let { recipesAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                    Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadDataFromCache() {

        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) {
                if (it.isNotEmpty())
                    recipesAdapter.setData(it[0].foodRecipe)
            }
        }
    }

    private fun visibleNotice() {
        binding.errorImage.visibility = View.VISIBLE
        binding.errorText.visibility = View.VISIBLE
    }

    private fun invisibleNotice() {
        binding.errorImage.visibility = View.INVISIBLE
        binding.errorText.visibility = View.INVISIBLE
    }

    private fun showShimmerEffect() {
        binding.shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.shimmerRecyclerView.hideShimmer()
    }
}