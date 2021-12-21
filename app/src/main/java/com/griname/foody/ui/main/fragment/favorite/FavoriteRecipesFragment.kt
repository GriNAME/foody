package com.griname.foody.ui.main.fragment.favorite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.griname.foody.R
import com.griname.foody.databinding.FragmentFavoriteRecipesBinding
import com.griname.foody.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {

    private val binding: FragmentFavoriteRecipesBinding by viewBinding(CreateMethod.INFLATE)
    private val mainViewModel by viewModels<MainViewModel>()
    private val favoriteAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), mainViewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) {
            if (it.isEmpty())
                visibleNotice()
            else
                invisibleNotice()
        }
    }

    private fun initRecyclerView() {
        binding.favoriteRecyclerView.adapter = favoriteAdapter
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) {
            favoriteAdapter.setData(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all_favorite_recipes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all_favorite_recipes_menu) {
            mainViewModel.deleteAllFavoriteRecipes()
            Snackbar.make(binding.root, getString(R.string.all_favorite_recipes_deleted), Snackbar.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun visibleNotice() {
        binding.noDataImage.visibility = View.VISIBLE
        binding.noDataText.visibility = View.VISIBLE
    }

    private fun invisibleNotice() {
        binding.noDataImage.visibility = View.INVISIBLE
        binding.noDataText.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteAdapter.clearContextualActionMode()
    }
}