package com.sudzusama.vkimageclassifier.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.ActivityMainBinding
import com.sudzusama.vkimageclassifier.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.toLoginFlow.observe(this, {
            binding.navHostFragment.findNavController().navigate(R.id.global_to_auth_nav_graph)
        })

        viewModel.toMainFlow.observe(this, {
            binding.navHostFragment.findNavController().navigate(R.id.global_to_main_nav_graph)
        })
    }


}