package com.ineedyourcode.decomposeme.ui.registration

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ineedyourcode.decomposeme.App
import com.ineedyourcode.decomposeme.data.EXTRA_LOGIN_SUCCESS
import com.ineedyourcode.decomposeme.databinding.ActivityRegistrationBinding
import com.ineedyourcode.decomposeme.ui.extentions.hideKeyboard
import com.ineedyourcode.decomposeme.ui.extentions.showSnack
import com.ineedyourcode.decomposeme.ui.login.LoginActivity

class RegistrationActivity : AppCompatActivity(), RegistrationActivityContract.RegistrationView {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var registrationPresenter: RegistrationActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registrationPresenter = RegistrationActivityPresenter(App.userRepository)
        registrationPresenter.onAttach(this)

        with(binding) {
            textEditRepeatPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(enteredChars: Editable?) {
                    if (enteredChars.toString() == passwordTextEdit.text.toString()) {
                        registrationButton.isEnabled = true
                    }
                }
            })

            registrationButton.setOnClickListener {
                registrationPresenter.onRegister(
                    textEditNewLogin.text.toString(),
                    textEditRepeatPassword.text.toString()
                )
            }
        }
    }

    override fun setRegistrationSuccess(login: String) {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            putExtra(EXTRA_LOGIN_SUCCESS, login)
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