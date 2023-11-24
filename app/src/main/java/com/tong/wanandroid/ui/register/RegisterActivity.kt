package com.tong.wanandroid.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.tong.wanandroid.R
import com.tong.wanandroid.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        binding.apply {
            vm = viewModel
            registerButton.setOnClickListener {
                val name = viewModel.userNameObservable.get()?.trim().toString()
                val password = viewModel.passwordObservable.get()?.trim().toString()
                val confirmPassword = viewModel.confirmPasswordObservable.get()?.trim().toString()
                if (checkRegisterStatus(name, password, confirmPassword)) {
                    viewModel.register(name, password, confirmPassword)
                    Loading.isVisible = true
                }
            }
            userName.doAfterTextChanged {
                if (userNameInputLayout.error.isNullOrBlank().not()) userNameInputLayout.error = ""
            }
            password.doAfterTextChanged {
                if (passwordInputLayout.error.isNullOrBlank().not()) passwordInputLayout.error = ""
            }
            confirmPassword.doAfterTextChanged {
                if (confirmPasswordInputLayout.error.isNullOrBlank()
                        .not()
                ) confirmPasswordInputLayout.error = ""
            }
        }

        viewModel.registerLiveData.observe(this) {
            if (it.isSuccess()) {
                finish()
                Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show()
            }
            binding.Loading.isVisible = false
        }
    }

    private fun checkRegisterStatus(
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (username.length < 3) {
            binding.userNameInputLayout.error = "用户名最少3位"
            return false
        }
        if (password.length < 6) {
            binding.passwordInputLayout.error = "密码至少 6 位"
            return false
        }
        if (confirmPassword.length < 6) {
            binding.confirmPasswordInputLayout.error = "密码至少 6 位"
            return false
        }
        if (password != confirmPassword) {
            binding.confirmPasswordInputLayout.error = "确认密码与密码不符"
            return false
        }
        return true
    }
}