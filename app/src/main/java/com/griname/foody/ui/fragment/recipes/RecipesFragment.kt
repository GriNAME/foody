package com.griname.foody.ui.fragment.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.griname.foody.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)

        binding.apply {
            shimmerRecyclerView.showShimmer()
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}