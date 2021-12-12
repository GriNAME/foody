package com.griname.foody.ui.details.instruction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.griname.foody.R
import com.griname.foody.data.network.model.Result
import com.griname.foody.databinding.FragmentInstructionBinding
import com.griname.foody.util.Constant.Companion.BUNDLE_RECIPE_RESULT_KEY

class InstructionFragment : Fragment(R.layout.fragment_instruction) {

    private val binding by viewBinding<FragmentInstructionBinding>(CreateMethod.INFLATE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Result? = arguments?.getParcelable(BUNDLE_RECIPE_RESULT_KEY)

        binding.apply {
            instructionWebView.webViewClient = object : WebViewClient() {}
            instructionWebView.loadUrl(bundle!!.sourceUrl)
        }

    }
}