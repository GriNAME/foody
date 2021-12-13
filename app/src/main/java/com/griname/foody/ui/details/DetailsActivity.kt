package com.griname.foody.ui.details

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.griname.foody.R
import com.griname.foody.data.database.entity.FavoriteEntity
import com.griname.foody.databinding.ActivityDetailsBinding
import com.griname.foody.ui.details.ingredients.IngredientsFragment
import com.griname.foody.ui.details.instruction.InstructionFragment
import com.griname.foody.ui.details.overview.OverviewFragment
import com.griname.foody.ui.main.viewmodel.MainViewModel
import com.griname.foody.util.Constant.Companion.BUNDLE_RECIPE_RESULT_KEY
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity(R.layout.activity_details) {

    private val TAG = "DetailsActivity"
    private val binding by viewBinding<ActivityDetailsBinding>(R.id.details_container)
    private val mainViewModel by viewModels<MainViewModel>()
    private val args by navArgs<DetailsActivityArgs>()

    private var savedRecipe = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>().apply {
            add(OverviewFragment())
            add(IngredientsFragment())
            add(InstructionFragment())
        }

        val titles = ArrayList<String>().apply {
            add("Overview")
            add("Ingredients")
            add("Instruction")
        }

        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_RECIPE_RESULT_KEY, args.result)

        val pagerAdapter = PagerAdapter(bundle, fragments, this)

        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorite)
        checkSavedRecipe(menuItem!!)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == android.R.id.home -> finish()
            item.itemId == R.id.save_to_favorite && !savedRecipe ->
                saveToFavoriteRecipe(item)
            item.itemId == R.id.save_to_favorite && savedRecipe ->
                removeSavedFavoriteRecipe(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipe(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { favoriteEntity ->
            try {
                for (savedRecipe in favoriteEntity) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuIcon(menuItem, R.drawable.ic_star)
                        savedRecipeId = savedRecipe.id
                        this.savedRecipe = true
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "checkSavedRecipe: ${e.message}")
            }
        }
    }

    private fun saveToFavoriteRecipe(menuItem: MenuItem) {
        val favoriteEntity = FavoriteEntity(0, args.result)
        mainViewModel.insertFavoriteRecipe(favoriteEntity)
        changeMenuIcon(menuItem, R.drawable.ic_star)
        showSnackBar("Add to Favorite.")
        savedRecipe = true
    }

    private fun removeSavedFavoriteRecipe(menuItem: MenuItem) {
        val favoriteEntity = FavoriteEntity(savedRecipeId, args.result)
        mainViewModel.deleteFavoriteRecipe(favoriteEntity)
        changeMenuIcon(menuItem, R.drawable.ic_star_outline)
        showSnackBar("Removed from Favorite.")
        savedRecipe = false
    }

    private fun changeMenuIcon(menuItem: MenuItem, icon: Int) {
        menuItem.icon = ContextCompat.getDrawable(this, icon)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Ok") {}
            .show()
    }
}