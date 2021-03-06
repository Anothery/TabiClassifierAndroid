package com.sudzusama.vkimageclassifier.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.ActivityMainBinding
import com.sudzusama.vkimageclassifier.ui.base.BaseActivity
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


}