package com.ineedyourcode.decomposeme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ineedyourcode.decomposeme.databinding.ActivityLoginBinding
import com.ineedyourcode.decomposeme.domain.contracts.LoginActivityContract
import com.ineedyourcode.decomposeme.presenter.LoginActivityPresenter
import com.ineedyourcode.decomposeme.ui.extensions.hideKeyboard
import com.ineedyourcode.decomposeme.ui.extensions.showSnack

class LoginActivity : AppCompatActivity(), LoginActivityContract.LoginView {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginPresenter: LoginActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginPresenter = LoginActivityPresenter().apply { onAttach(this@LoginActivity) }

        binding.btnLogin.setOnClickListener {
            loginPresenter.onLogin(
                binding.textEditLogin.text.toString(),
                binding.textEditPassword.text.toString()
            )
        }

        binding.btnForgotPassword.setOnClickListener {
            loginPresenter.onPasswordRemind(binding.textEditLogin.text.toString())
        }
    }

    override fun setLoginSuccess(login: String) {
        binding.root.apply {
            hideKeyboard()
            showSnack("Добро пожаловать, $login")
        }
    }

    override fun setLoginError(message: String) {
        binding.root.apply {
            hideKeyboard()
            showSnack(message)
        }
    }

    override fun showRemindedPassword(remindedPassword: String) {
        binding.root.apply {
            hideKeyboard()
            showSnack(remindedPassword)
        }
    }

    override fun showProgress() {
//        TODO("Not yet implemented")
    }

    override fun hideProgress() {
//        TODO("Not yet implemented")
    }
}