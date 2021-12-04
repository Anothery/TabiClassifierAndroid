package com.sudzusama.vkimageclassifier.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.ActivityMainBinding
import com.sudzusama.vkimageclassifier.ui.base.BaseActivity
import com.sudzusama.vkimageclassifier.ui.imagedetail.ImageDetailParentFragment
import com.sudzusama.vkimageclassifier.utils.OnBackPressedListener
import com.vk.api.sdk.utils.VKUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.toLoginFlow.observe(this, {
            binding.navHostFragment.findNavController().navigate(R.id.global_to_auth_nav_graph)

        })
        viewModel.toMainFlow.observe(this, {
            binding.navHostFragment.findNavController().navigate(R.id.global_to_main_nav_graph)
        })
    }

    override fun onBackPressed() {
        val detailsFragment =
            (supportFragmentManager.findFragmentByTag(ImageDetailParentFragment.TAG) as? ImageDetailParentFragment)
        detailsFragment?.let {
            if (it.isVisible) (it as? OnBackPressedListener)?.onBackPressed()
        } ?: if (!binding.navHostFragment.findNavController().popBackStack()) finish()
    }
}