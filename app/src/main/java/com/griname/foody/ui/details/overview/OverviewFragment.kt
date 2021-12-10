package com.griname.foody.ui.details.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.griname.foody.R
import com.griname.foody.data.network.model.Result
import com.griname.foody.databinding.FragmentOverviewBinding
import com.griname.foody.util.Constant.Companion.BUNDLE_RECIPE_ARGS
import org.jsoup.Jsoup

class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val binding by viewBinding<FragmentOverviewBinding>(CreateMethod.INFLATE)
    private var recipeBundle: Result? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        recipeBundle = arguments?.getParcelable<Result>(BUNDLE_RECIPE_ARGS)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            mainImage.load(recipeBundle?.image)
            timeText.text = recipeBundle?.readyInMinutes.toString()
            likeText.text = recipeBundle?.aggregateLikes.toString()
            titleText.text = recipeBundle?.title
            recipeBundle?.summary.let {
                summaryText.text = Jsoup.parse(it).text()
            }

            if (recipeBundle?.vegetarian == true) {
                vegetarianImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                vegetarianText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (recipeBundle?.vegan == true) {
                veganImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                veganText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (recipeBundle?.glutenFree == true) {
                glutenFreeImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                glutenFreeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (recipeBundle?.dairyFree == true) {
                dairyFreeImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                dairyFreeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (recipeBundle?.veryHealthy == true) {
                healthyImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                healthyText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (recipeBundle?.cheap == true) {
                cheapImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                cheapText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }
    }
}
