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
import androidx.recyclerview.widget.LinearLayoutManager
import com.griname.foody.viewmodel.MainViewModel
import com.griname.foody.adapter.RecipesAdapter
import com.griname.foody.data.database.RecipeEntity
import com.griname.foody.databinding.FragmentRecipesBinding
import com.griname.foody.util.Constant.Companion.API_KEY
import com.griname.foody.util.NetworkResult
import com.griname.foody.util.observeOnce
import com.griname.foody.viewmodel.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Error

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private val TAG = "RecipesFragment"
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val recipesAdapter by lazy { RecipesAdapter() }
    private val mainViewModel by viewModels<MainViewModel>()
    private val recipesViewModel by viewModels<RecipesViewModel>()
    private val mediatorLiveData = MediatorLiveData<Boolean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediatorLiveData.addSource(mainViewModel.readRecipes) {
            mediatorLiveData.value = !it.isNullOrEmpty()
        }

        initRecyclerView()
        readDatabase()

        mediatorLiveData.addSource(mainViewModel.recipeResponse) {
            when(it) {
                is NetworkResult.Error -> {
                    mediatorLiveData.value = false
                    binding.errorText.text = it.message
                }
                is NetworkResult.Loading -> mediatorLiveData.value = false
                is NetworkResult.Success -> mediatorLiveData.value = true
            }
        }

        mediatorLiveData.observe(viewLifecycleOwner) {
            if (it) {
                invisibleNotice()
            } else {
                visibleNotice()
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

    private fun initRecyclerView() {
        binding.shimmerRecyclerView.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
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
                    response.data?.let { recipesAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), "Doesn't work!", Toast.LENGTH_SHORT).show()
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

    private fun showShimmerEffect() {
        binding.shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.shimmerRecyclerView.hideShimmer()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}