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
import com.griname.foody.util.Constant.Companion.BUNDLE_RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val binding by viewBinding<FragmentOverviewBinding>(CreateMethod.INFLATE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Result? = arguments?.getParcelable(BUNDLE_RECIPE_RESULT_KEY)

        binding.apply {
            mainImage.load(bundle?.image)
            timeText.text = bundle?.readyInMinutes.toString()
            likeText.text = bundle?.aggregateLikes.toString()
            titleText.text = bundle?.title
            bundle?.summary.let {
                summaryText.text = Jsoup.parse(it).text()
            }

            if (bundle?.vegetarian == true) {
                vegetarianImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                vegetarianText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (bundle?.vegan == true) {
                veganImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                veganText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (bundle?.glutenFree == true) {
                glutenFreeImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                glutenFreeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (bundle?.dairyFree == true) {
                dairyFreeImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                dairyFreeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (bundle?.veryHealthy == true) {
                healthyImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                healthyText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (bundle?.cheap == true) {
                cheapImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                cheapText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }
    }
}
