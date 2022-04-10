package com.ineedyourcode.decomposeme.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
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

        val registrationActivityIntent = Intent(this, RegistrationActivity::class.java)

        with(binding) {
            intent.getStringExtra(EXTRA_LOGIN_SUCCESS)?.let {
                val registeredLogin = it
                textEditLogin.setText(registeredLogin)
                root.showSnack(getString(R.string.registration_success, registeredLogin))
            }

            loginPresenter = LoginActivityPresenter().apply {
                onAttach(this@LoginActivity)
            }

            btnRegistration.setOnClickListener {
                startActivity(registrationActivityIntent)
            }

//        loginPresenter = restorePresenter().apply { onAttach(this@LoginActivity) }

            btnLogin.setOnClickListener {
                loginPresenter.onLogin(
                    textEditLogin.text.toString(),
                    textEditPassword.text.toString()
                )
            }

            btnForgotPassword.setOnClickListener {
                loginPresenter.onPasswordRemind(textEditLogin.text.toString())
            }

            btnAccountExit.setOnClickListener {
                loginPresenter.onAccountExit()
            }
        }
    }

//    private fun restorePresenter(): LoginActivityPresenter {
//        val presenter = lastCustomNonConfigurationInstance as? LoginActivityPresenter
//        return presenter ?: LoginActivityPresenter()
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onRetainCustomNonConfigurationInstance(): Any {
//        return loginPresenter
//    }

    override fun setLoginSuccess(login: String) {
        with(binding) {
            root.apply {
                hideKeyboard()
                showSnack(getString(R.string.hello_user, login))
            }

            loginGroup.isVisible = false
            authorizedGroup.isVisible = true
            btnAdminUserList.isVisible = false
            tvHelloUser.text = getString(R.string.hello_user, login)
            textEditLogin.text?.clear()
            textEditPassword.text?.clear()
        }
    }

    override fun setAdminLoginSuccess() {
        binding.btnAdminUserList.apply {
            isVisible = true
            setOnClickListener {
                binding.tvAdminUserList.text = loginPresenter.getUserList().toString()
            }
        }
    }

    override fun setLoginError(error: String) {
        binding.root.apply {
            hideKeyboard()
            showSnack(error)
        }
    }

    override fun exitAccount() {
        with(binding) {
            tvHelloUser.text = getString(R.string.empty_text)
            authorizedGroup.isVisible = false
            loginGroup.isVisible = true
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

//binding.btnGetUserList.setOnClickListener {
//    binding.tvUserList.text = loginPresenter.getUserList().toString()
//}
//
//binding.btnDeleteUser.setOnClickListener {
//    loginPresenter.delUser(binding.textEditLogin.text.toString())
//
//    binding.tvUserList.text = loginPresenter.getUserList().toString()
//}