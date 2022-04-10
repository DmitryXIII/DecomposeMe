package com.ineedyourcode.decomposeme.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.databinding.ActivityLoginBinding
import com.ineedyourcode.decomposeme.domain.EXTRA_LOGIN_SUCCESS
import com.ineedyourcode.decomposeme.presenter.login.LoginActivityContract
import com.ineedyourcode.decomposeme.presenter.login.LoginActivityPresenter
import com.ineedyourcode.decomposeme.ui.extentions.hideKeyboard
import com.ineedyourcode.decomposeme.ui.extentions.showSnack
import com.ineedyourcode.decomposeme.ui.registration.RegistrationActivity

class LoginActivity : AppCompatActivity(), LoginActivityContract.LoginView {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginPresenter: LoginActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra(EXTRA_LOGIN_SUCCESS)?.let {
            val registeredLogin = it
            binding.textEditLogin.setText(registeredLogin)
            binding.root.showSnack(getString(R.string.registration_success, registeredLogin))
        }

        loginPresenter = restorePresenter().apply { onAttach(this@LoginActivity) }

        binding.btnGetUserList.setOnClickListener {
            binding.tvUserList.text = loginPresenter.getUserList().toString()
        }

        binding.btnDeleteUser.setOnClickListener {
            loginPresenter.delUser(binding.textEditLogin.text.toString())

            binding.tvUserList.text = loginPresenter.getUserList().toString()
        }

        binding.btnLogin.setOnClickListener {
            loginPresenter.onLogin(
                binding.textEditLogin.text.toString(),
                binding.textEditPassword.text.toString()
            )
        }

        binding.btnRegistration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        binding.btnForgotPassword.setOnClickListener {
            loginPresenter.onPasswordRemind(binding.textEditLogin.text.toString())
        }
    }

    private fun restorePresenter(): LoginActivityPresenter {
        val presenter = lastCustomNonConfigurationInstance as? LoginActivityPresenter
        return presenter ?: LoginActivityPresenter()
    }

    @Deprecated("Deprecated in Java")
    override fun onRetainCustomNonConfigurationInstance(): Any {
        return loginPresenter
    }

    override fun setLoginSuccess(login: String) {
        binding.root.apply {
            hideKeyboard()
            showSnack(getString(R.string.hello_user, login))
        }
    }

    override fun setLoginError(error: String) {
        binding.root.apply {
            hideKeyboard()
            showSnack(error)
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