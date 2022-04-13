package com.ineedyourcode.decomposeme.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ineedyourcode.decomposeme.App
import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.data.EXTRA_LOGIN_SUCCESS
import com.ineedyourcode.decomposeme.databinding.ActivityLoginBinding
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
                loginTextEdit.setText(registeredLogin)
                root.showSnack(getString(R.string.registration_success, registeredLogin))
                intent.removeExtra(EXTRA_LOGIN_SUCCESS)
            }

            loginPresenter = restorePresenter().apply { onAttach(this@LoginActivity) }

            registrationButton.setOnClickListener {
                startActivity(registrationActivityIntent)
                finish()
            }

            loginButton.setOnClickListener {
                loginPresenter.onLogin(
                    loginTextEdit.text.toString(),
                    passwordTextEdit.text.toString()
                )
            }

            forgotPasswordButton.setOnClickListener {
                loginPresenter.onPasswordRemind(loginTextEdit.text.toString())
            }

            logoutButton.setOnClickListener {
                loginPresenter.onLogout()
            }
        }
    }

    private fun restorePresenter(): LoginActivityPresenter {
        val presenter = lastCustomNonConfigurationInstance as? LoginActivityPresenter
        return presenter ?: LoginActivityPresenter(App.userRepository, App.userLoginInteractor, App.userRemindPasswordInteractor)
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
            helloUserTextView.text = getString(R.string.hello_user, login)
            loginTextEdit.text?.clear()
            passwordTextEdit.text?.clear()
        }
    }

    override fun setAdminLoginSuccess() {
        binding.adminGroup.isVisible = true
        binding.adminUserListButton.apply {
            setOnClickListener {
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

    override fun setLogout() {
        with(binding) {
            helloUserTextView.text = getString(R.string.empty_text)
            adminUserListTextView.text = getString(R.string.empty_text)
            authorizedGroup.isVisible = false
            adminGroup.isVisible = false
            loginGroup.isVisible = true
        }
    }

    override fun showUserList(userList: String) {
        binding.adminUserListTextView.text = userList
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
}