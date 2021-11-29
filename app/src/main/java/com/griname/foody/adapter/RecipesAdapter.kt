package com.griname.foody.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.griname.foody.R
import com.griname.foody.databinding.RowRecipesLayoutBinding
import com.griname.foody.model.FoodRecipe
import com.griname.foody.model.Result
import com.griname.foody.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    private var recipes = emptyList<Result>()

    fun setData(newData: FoodRecipe) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val binding: RowRecipesLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Result) {
            binding.apply {
                recipeTitle.text = recipe.title
                recipeDescription.text = recipe.summary
                recipeHeartText.text = recipe.aggregateLikes.toString()
                recipeTimeText.text = recipe.readyInMinutes.toString()

                if (recipe.vegan) {
                    recipeLeafText.setTextColor(ContextCompat.getColor(root.context, R.color.green))
                    recipeLeafText.text = "Vegan"
                    recipeLeafImage.setColorFilter(ContextCompat.getColor(root.context, R.color.green))
                }

                recipeImage.load(recipe.image) {
                    crossfade(600)
                    error(R.drawable.ic_error_placeholder)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowRecipesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(recipes[position])

    override fun getItemCount(): Int = recipes.size
}