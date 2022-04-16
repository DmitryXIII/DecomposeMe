package com.ineedyourcode.decomposeme.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ineedyourcode.decomposeme.App
import com.ineedyourcode.decomposeme.databinding.ActivityRegistrationBinding
import com.ineedyourcode.decomposeme.ui.login.LoginActivity
import com.ineedyourcode.decomposeme.ui.uiutils.hideKeyboard
import com.ineedyourcode.decomposeme.ui.uiutils.setOnTextTypingListener
import com.ineedyourcode.decomposeme.ui.uiutils.showSnack

class RegistrationActivity : AppCompatActivity(), RegistrationActivityContract.RegistrationView {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var registrationPresenter: RegistrationActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registrationPresenter = RegistrationActivityPresenter(App.userRegistrationInteractor)
        registrationPresenter.onAttach(this)

        with(binding) {
            repeatPasswordTextEdit.setOnTextTypingListener { enteredChars ->
                if (enteredChars.toString() == passwordTextEdit.text.toString()) {
                    registrationButton.isEnabled = true
                }
            }

            registrationButton.setOnClickListener {
                registrationPresenter.onRegister(
                    newLoginTextEdit.text.toString(),
                    repeatPasswordTextEdit.text.toString()
                )
            }
        }
    }

    override fun setRegistrationSuccess(login: String) {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            putExtra(LoginActivity.EXTRA_LOGIN_REGISTRATION_SUCCESS, login)
        })
        finish()
    }

    override fun setRegistrationError(error: String) {
        binding.root.showSnack(error)
    }

    override fun showProgress() {
        binding.progressBar.apply {
            isVisible = true
            hideKeyboard()
        }
    }

    override fun hideProgress() {
        binding.progressBar.isVisible = false
    }
}