package com.sudzusama.vkimageclassifier.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentAuthBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            activity?.let { viewModel.onLogin(it as AppCompatActivity) }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                viewModel.onLoginSuccess(token)
            }

            override fun onLoginFailed(errorCode: Int) {
                viewModel.onError(errorCode)
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}