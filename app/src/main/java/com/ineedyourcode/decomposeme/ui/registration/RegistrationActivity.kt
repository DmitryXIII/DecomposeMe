package com.ineedyourcode.decomposeme.ui.registration

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ineedyourcode.decomposeme.App
import com.ineedyourcode.decomposeme.databinding.ActivityRegistrationBinding
import com.ineedyourcode.decomposeme.ui.login.LoginActivity
import com.ineedyourcode.decomposeme.ui.utils.hideKeyboard
import com.ineedyourcode.decomposeme.ui.utils.setOnTextTypingListener
import com.ineedyourcode.decomposeme.ui.utils.showSnack

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private var registrationViewModel: RegistrationActivityViewModel? = null
    private val uiHandler: Handler by lazy { Handler(mainLooper) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registrationViewModel = restoreViewModel()

        registrationViewModel?.messenger?.subscribe(uiHandler) { message ->
            message?.let {
                showMessage(getString(message.first, message.second))
            }
        }

        registrationViewModel?.registrationSuccess?.subscribe(uiHandler) { login ->
            login?.let {
                setRegistrationSuccess(login)
            }
        }

        registrationViewModel?.isInProgress?.subscribe(uiHandler) { isInProgress ->
            if (isInProgress == true) {
                showProgress()
            } else {
                hideProgress()
            }
        }

        with(binding) {
            repeatPasswordTextEdit.setOnTextTypingListener { enteredChars ->
                if (enteredChars.toString() == passwordTextEdit.text.toString()) {
                    registrationButton.isEnabled = true
                }
            }

            registrationButton.setOnClickListener {
                registrationViewModel?.onRegister(
                    newLoginTextEdit.text.toString(),
                    repeatPasswordTextEdit.text.toString()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        registrationViewModel?.messenger?.unsubscribeAll()
    }

    @Deprecated("Deprecated in Java")
    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return registrationViewModel
    }

    private fun restoreViewModel(): RegistrationActivityViewModel {
        val registrationViewModel =
            lastCustomNonConfigurationInstance as? RegistrationActivityViewModel
        return registrationViewModel
            ?: RegistrationActivityViewModel(App.userRegistrationInteractor)
    }

    private fun setRegistrationSuccess(login: String) {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            putExtra(LoginActivity.EXTRA_LOGIN_REGISTRATION_SUCCESS, login)
        })
        finish()
    }

    private fun showMessage(message: String) {
        binding.root.showSnack(message)
    }

    private fun showProgress() {
        binding.progressBar.apply {
            isVisible = true
            hideKeyboard()
        }
    }

    private fun hideProgress() {
        binding.progressBar.isVisible = false
    }
}