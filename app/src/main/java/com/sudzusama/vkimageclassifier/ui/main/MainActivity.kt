package com.sudzusama.vkimageclassifier.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudzusama.vkimageclassifier.AuthNavGraphDirections
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.ActivityMainBinding
import com.sudzusama.vkimageclassifier.ui.base.BaseActivity
import com.sudzusama.vkimageclassifier.ui.imagedetail.ImageDetailParentFragment
import com.sudzusama.vkimageclassifier.utils.view.OnBackPressedListener
import com.sudzusama.vkimageclassifier.utils.view.shortToast
import com.vk.api.sdk.utils.VKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.toLoginFlow.observe(this) {
            binding.navHostFragment.findNavController()
                .navigate(AuthNavGraphDirections.globalToAuthNavGraph())

        }
        viewModel.toMainFlow.observe(this) {
            binding.navHostFragment.findNavController()
                .navigate(AuthNavGraphDirections.globalToMainNavGraph())
        }
    }

    override fun onBackPressed() {
        val detailsFragment =
            (supportFragmentManager.findFragmentByTag(ImageDetailParentFragment.TAG) as? ImageDetailParentFragment)
        detailsFragment?.let {
            if (it.isVisible) (it as? OnBackPressedListener)?.onBackPressed()
        } ?: super.onBackPressed()
    }
}