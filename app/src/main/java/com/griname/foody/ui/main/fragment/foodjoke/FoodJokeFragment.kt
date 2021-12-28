package com.griname.foody.ui.main.fragment.foodjoke

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.griname.foody.R
import com.griname.foody.databinding.FragmentFoodJokeBinding
import com.griname.foody.ui.main.viewmodel.MainViewModel
import com.griname.foody.util.Constant.Companion.API_KEY
import com.griname.foody.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment(R.layout.fragment_food_joke) {

    private val mainViewModel by viewModels<MainViewModel>()
    private val mediatorLiveData = MediatorLiveData<Boolean>()
    private val binding: FragmentFoodJokeBinding by viewBinding(CreateMethod.INFLATE)

    private var foodJoke = "No Food Joke"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.foodJokeText.text = response.data?.text

                    if (response.data != null)
                        foodJoke = response.data.text
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_joke_menu) {
            val sharedIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                this.type = "text/plain"
            }
            startActivity(sharedIntent)
        }
        return super.onOptionsItemSelected(item)
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
                    binding.foodJokeErrorText.text = it.message
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


    private fun loadDataFromCache() {

        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    binding.foodJokeText.text = it.first().foodJoke.text
                    foodJoke = it.first().foodJoke.text
                }
            }
        }
    }

    private fun visibleNotice() {
        binding.foodJokeErrorImage.visibility = View.VISIBLE
        binding.foodJokeErrorText.visibility = View.VISIBLE
    }

    private fun invisibleNotice() {
        binding.foodJokeErrorImage.visibility = View.INVISIBLE
        binding.foodJokeErrorText.visibility = View.INVISIBLE
    }

}