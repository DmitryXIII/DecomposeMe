package com.ineedyourcode.decomposeme.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.databinding.ActivityLoginBinding
import com.ineedyourcode.decomposeme.domain.EXTRA_LOGIN_SUCCESS
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
                intent.removeExtra(EXTRA_LOGIN_SUCCESS)
            }

            loginPresenter = restorePresenter().apply { onAttach(this@LoginActivity) }

            btnRegistration.setOnClickListener {
                startActivity(registrationActivityIntent)
                finish()
            }

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

    private fun restorePresenter(): LoginActivityPresenter {
        val presenter = lastCustomNonConfigurationInstance as? LoginActivityPresenter
        return presenter ?: LoginActivityPresenter()
    }

    @Deprecated("Deprecated in Java")
    override fun onRetainCustomNonConfigurationInstance(): Any {
        return loginPresenter
    }

    override fun setLoginSuccess(login: String) {
        with(binding) {
            root.hideKeyboard()
            authorizedGroup.isVisible = true
            loginGroup.isVisible = false
            adminGroup.isVisible = false
            tvHelloUser.text = getString(R.string.hello_user, login)
            textEditLogin.text?.clear()
            textEditPassword.text?.clear()
        }
    }

    override fun setAdminLoginSuccess() {
        binding.btnAdminUserList.apply {
            isVisible = true
            setOnClickListener {
                binding.adminScroll.isVisible = true
                loginPresenter.getUserList()
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
            adminGroup.isVisible = false
            loginGroup.isVisible = true
        }
    }

    override fun showUserList(text: String) {
        binding.tvAdminUserList.text = text
    }

    override fun showRemindedPassword(remindedPassword: String) {
        binding.root.apply {
            hideKeyboard()
            showSnack(remindedPassword)
        }
    }

    override fun showProgress() {
        binding.progressBar.apply {
            hideKeyboard()
            isVisible = true
        }
    }

    override fun hideProgress() {
        binding.progressBar.isVisible = false
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.extras != null) {
            if (intent.extras!!.containsKey(EXTRA_LOGIN_SUCCESS)) {
                val registeredLogin = intent.extras!!.getString(EXTRA_LOGIN_SUCCESS)
                binding.textEditLogin.setText(registeredLogin)
                binding.root.showSnack(getString(R.string.registration_success, registeredLogin))
            }
        }
    }
}