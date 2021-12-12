package com.griname.foody.ui.details.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.griname.foody.R
import com.griname.foody.data.network.model.ExtendedIngredient
import com.griname.foody.databinding.RowIngredientsLayoutBinding
import com.griname.foody.util.Constant.Companion.BASE_IMAGE_URL
import com.griname.foody.util.RecipesDiffUtil

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var ingredients = emptyList<ExtendedIngredient>()

    fun setData(ingredients: List<ExtendedIngredient>) {
        val ingredientDiffUtil = RecipesDiffUtil(this.ingredients, ingredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientDiffUtil)
        this.ingredients = ingredients
        diffUtilResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val binding: RowIngredientsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: ExtendedIngredient) {
            binding.apply {
                ingredientTitle.text = ingredient.name.uppercase()
                ingredientAmount.text = ingredient.amount.toString()
                ingredientUnit.text = ingredient.unit
                ingredientConsistency.text = ingredient.consistency
                ingredientOriginal.text = ingredient.original
                ingredientImage.load(BASE_IMAGE_URL +  ingredient.image) {
                    crossfade(600)
                    error(R.drawable.ic_error_placeholder)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowIngredientsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(ingredients[position])

    override fun getItemCount(): Int = ingredients.size
}