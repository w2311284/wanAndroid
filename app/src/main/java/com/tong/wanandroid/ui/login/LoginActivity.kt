package com.tong.wanandroid.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.tong.wanandroid.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        viewModel.loginLiveData.observe(this@LoginActivity) {
            updateLoginLoadingStatus(false)
            if (it.isSuccess()) finish()
        }
        binding.backIcon.setOnClickListener {
            finish()
        }
        binding.register.setOnClickListener {

        }
        binding.loginButton.setOnClickListener {
            updateLoginLoadingStatus(true)
            viewModel.loginIn(viewModel.userNameObservable.get().toString(),viewModel.passwordObservable.get().toString())
        }
    }

    private fun updateLoginLoadingStatus(isLoading: Boolean) {
        binding.loginLoading.isVisible = isLoading
    }
}