package com.tong.wanandroid.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tong.wanandroid.common.store.UserInfoManager
import com.tong.wanandroid.databinding.ActivityLoginBinding
import com.tong.wanandroid.ui.register.RegisterActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
            lifecycleScope.launch {
                UserInfoManager.getInstance(this@LoginActivity).setLoggedInState(true)
            }
            if (it.isSuccess()) finish()

        }
        binding.viewModel = viewModel
        binding.backIcon.setOnClickListener {
            finish()
        }
        binding.loginButton.setOnClickListener {
            updateLoginLoadingStatus(true)
            viewModel.loginIn(viewModel.userNameObservable.get().toString(),viewModel.passwordObservable.get().toString())
        }
        binding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateLoginLoadingStatus(isLoading: Boolean) {
        binding.loginLoading.isVisible = isLoading
    }


}