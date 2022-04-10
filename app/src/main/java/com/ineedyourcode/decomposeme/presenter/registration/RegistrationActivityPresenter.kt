package com.ineedyourcode.decomposeme.presenter.registration

import android.os.Handler
import android.os.Looper
import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.*
import com.ineedyourcode.decomposeme.domain.repository.IUserRepository
import com.ineedyourcode.decomposeme.domain.repository.UserRepository
import com.ineedyourcode.decomposeme.ui.registration.RegistrationActivity

class RegistrationActivityPresenter : RegistrationActivityContract.RegistrationPresenter {
    private val userRepository: IUserRepository = UserRepository(App.getUserDao())

    private val uiThread = Handler(Looper.getMainLooper())

    private lateinit var view: RegistrationActivityContract.RegistrationView

    override fun onAttach(mView: RegistrationActivityContract.RegistrationView) {
        view = mView
    }

    override fun onRegister(login: String, password: String) {
        if (login.isBlank()) {
            view.setRegistrationError((view as RegistrationActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            uiThread.postDelayed({
                when (userRepository.addNewUser(login, password)) {
                    REQUEST_CODE_OK -> {
                        view.hideProgress()
                        view.setRegistrationSuccess(login)
                    }
                    REQUEST_CODE_LOGIN_REGISTERED_YET -> {
                        view.hideProgress()
                        view.setRegistrationError(
                            (view as RegistrationActivity).getString(
                                R.string.login_registered_yet,
                                login
                            )
                        )
                    }
                }
            }, fakeDelay())
        }
    }
}