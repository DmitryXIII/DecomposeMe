package com.ineedyourcode.decomposeme.ui.registration

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.interactor.registration.IUserRegistrationInteractor

private enum class ResponseCodes(val code: Int) {
    RESPONSE_SUCCESS(200),
    RESPONSE_LOGIN_REGISTERED_YET(444)
}

class RegistrationActivityPresenter(private val userRegistrationInteractor: IUserRegistrationInteractor) :
    RegistrationActivityContract.RegistrationPresenter {

    private lateinit var view: RegistrationActivityContract.RegistrationView

    override fun onAttach(mView: RegistrationActivityContract.RegistrationView) {
        view = mView
    }

    override fun onRegister(login: String, password: String) {
        if (login.isBlank()) {
            view.setRegistrationError((view as RegistrationActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            userRegistrationInteractor.userRegistration(login, password) { response ->
                when (response) {
                    ResponseCodes.RESPONSE_SUCCESS.code -> {
                        view.hideProgress()
                        view.setRegistrationSuccess(login)
                    }
                    ResponseCodes.RESPONSE_LOGIN_REGISTERED_YET.code -> {
                        view.hideProgress()
                        view.setRegistrationError(
                            (view as RegistrationActivity).getString(
                                R.string.login_registered_yet,
                                login
                            )
                        )
                    }
                }
            }
        }
    }
}