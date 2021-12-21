package com.griname.foody.ui.main.fragment.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.griname.foody.R
import com.griname.foody.data.database.entity.FavoriteEntity
import com.griname.foody.databinding.RowRecipesLayoutBinding
import com.griname.foody.util.RecipesDiffUtil
import org.jsoup.Jsoup

class FavoriteRecipesAdapter : RecyclerView.Adapter<FavoriteRecipesAdapter.ViewHolder>() {

    private var recipes = emptyList<FavoriteEntity>()

    fun setData(favoriteEntities: List<FavoriteEntity>) {
        val favoriteDiffUtil = RecipesDiffUtil(recipes, favoriteEntities)
        val resultDiffUtil = DiffUtil.calculateDiff(favoriteDiffUtil)
        recipes = favoriteEntities
        resultDiffUtil.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val binding: RowRecipesLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: FavoriteEntity) {
            binding.apply {
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
                    val action = FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(recipe.result)
                    root.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipesAdapter.ViewHolder {
        val binding =
            RowRecipesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteRecipesAdapter.ViewHolder, position: Int) =
        holder.bind(recipes[position])

    override fun getItemCount(): Int = recipes.size
}