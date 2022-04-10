package com.ineedyourcode.decomposeme.ui.registration

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.ineedyourcode.decomposeme.databinding.ActivityRegistrationBinding
import com.ineedyourcode.decomposeme.domain.EXTRA_LOGIN_SUCCESS
import com.ineedyourcode.decomposeme.presenter.registration.RegistrationActivityContract
import com.ineedyourcode.decomposeme.presenter.registration.RegistrationActivityPresenter
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

        registrationPresenter = RegistrationActivityPresenter()
        registrationPresenter.onAttach(this)

        binding.textEditRepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(enteredChars: Editable?) {
                if (enteredChars.toString() == binding.textEditPassword.text.toString()) {
                    binding.btnRegistration.isEnabled = true
                }
            }
        })

        binding.btnRegistration.setOnClickListener {
            registrationPresenter.onRegister(
                binding.textEditNewLogin.text.toString(),
                binding.textEditRepeatPassword.text.toString()
            )
        }
    }

    override fun setRegistrationSuccess(login: String) {
        binding.root.hideKeyboard()
        startActivity(Intent(this, LoginActivity::class.java).apply {
            putExtra(EXTRA_LOGIN_SUCCESS, login)
        })
        finish()
    }

    override fun setRegistrationError(error: String) {
        binding.root.showSnack(error)
    }

    override fun showProgress() {
//        TODO("Not yet implemented")
    }

    override fun hideProgress() {
//        TODO("Not yet implemented")
    }
}