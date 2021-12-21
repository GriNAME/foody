package com.griname.foody.ui.main.fragment.favorite

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.griname.foody.R
import com.griname.foody.data.database.entity.FavoriteEntity
import com.griname.foody.databinding.RowRecipesLayoutBinding
import com.griname.foody.ui.main.viewmodel.MainViewModel
import com.griname.foody.util.RecipesDiffUtil
import org.jsoup.Jsoup

class FavoriteRecipesAdapter(
    private val requiredActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteRecipesAdapter.ViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private lateinit var actionMode: ActionMode
    private lateinit var rootView: View

    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var viewHolders = arrayListOf<ViewHolder>()
    private var recipes = emptyList<FavoriteEntity>()

    fun setData(favoriteEntities: List<FavoriteEntity>) {
        val favoriteDiffUtil = RecipesDiffUtil(recipes, favoriteEntities)
        val resultDiffUtil = DiffUtil.calculateDiff(favoriteDiffUtil)
        recipes = favoriteEntities
        resultDiffUtil.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: RowRecipesLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: FavoriteEntity) {
            binding.apply {
                rootView = root

                recipeTitle.text = recipe.result.title
                recipeDescription.text = Jsoup.parse(recipe.result.summary).text()
                recipeHeartText.text = recipe.result.aggregateLikes.toString()
                recipeTimeText.text = recipe.result.readyInMinutes.toString()

                if (recipe.result.vegan) {
                    recipeLeafText.setTextColor(ContextCompat.getColor(root.context, R.color.green))
                    recipeLeafText.text = root.context.getString(R.string.vegan)
                    recipeLeafImage.setColorFilter(ContextCompat.getColor(root.context, R.color.green))
                }

                recipeImage.load(recipe.result.image) {
                    crossfade(600)
                    error(R.drawable.ic_error_placeholder)
                }

                root.setOnClickListener {
                    if (multiSelection) {
                        applySelection(recipe)
                    } else {
                        val action =
                            FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(recipe.result)
                        root.findNavController().navigate(action)
                    }

                }

                root.setOnLongClickListener {
                    if (!multiSelection) {
                        multiSelection = true
                        requiredActivity.startActionMode(this@FavoriteRecipesAdapter)
                        applySelection(recipe)
                        true
                    } else {
                        multiSelection = false
                        false
                    }
                }
            }
        }

        fun changeRecipeStyle(backgroundColor: Int, strokeColor: Int) {
            binding.recipeConstraintLayout.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                backgroundColor))
            binding.recipeCard.strokeColor = ContextCompat.getColor(binding.root.context, strokeColor)
        }

        private fun applySelection(recipe: FavoriteEntity) {
            if (selectedRecipes.contains(recipe)) {
                selectedRecipes.remove(recipe)
                changeRecipeStyle(R.color.white, R.color.strokeColor)
                applyActionModeTitle()
            } else {
                selectedRecipes.add(recipe)
                changeRecipeStyle(R.color.cardBackgroundLightColor, R.color.colorPrimary)
                applyActionModeTitle()
            }
        }
    }

    // Implementation menu
    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        this.actionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_item_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            when (selectedRecipes.size) {
                1 -> Snackbar.make(rootView, "${selectedRecipes.size} item deleted", Snackbar.LENGTH_LONG).show()
                else -> Snackbar.make(rootView, "${selectedRecipes.size} items deleted", Snackbar.LENGTH_LONG).show()
            }
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        viewHolders.forEach { holder ->
            holder.changeRecipeStyle(R.color.cardNormalBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requiredActivity.window.statusBarColor = ContextCompat.getColor(requiredActivity, color)
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                actionMode.finish()
            }
            1 -> {
                actionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                actionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    fun clearContextualActionMode() {
        if (this::actionMode.isInitialized) {
            actionMode.finish()
        }
    }

    // Create RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipesAdapter.ViewHolder {
        val binding =
            RowRecipesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteRecipesAdapter.ViewHolder, position: Int) {
        viewHolders.add(holder)
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size
}