package com.griname.foody.ui.details

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.griname.foody.R
import com.griname.foody.databinding.ActivityDetailsBinding
import com.griname.foody.ui.details.ingredients.IngredientsFragment
import com.griname.foody.ui.details.instruction.InstructionFragment
import com.griname.foody.ui.details.overview.OverviewFragment
import com.griname.foody.util.Constant.Companion.BUNDLE_RECIPE_RESULT_KEY

class DetailsActivity : AppCompatActivity(R.layout.activity_details) {

    private val binding by viewBinding<ActivityDetailsBinding>(R.id.details_container)
    private val args by navArgs<DetailsActivityArgs>()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }
}