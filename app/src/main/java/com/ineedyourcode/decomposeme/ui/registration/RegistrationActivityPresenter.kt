package com.ineedyourcode.decomposeme.ui.registration

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.data.REQUEST_CODE_LOGIN_REGISTERED_YET
import com.ineedyourcode.decomposeme.data.REQUEST_CODE_OK
import com.ineedyourcode.decomposeme.domain.interactor.registration.IUserRegistrationInteractor

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
            userRegistrationInteractor.addNewUser(login, password) { response ->
                when (response) {
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
            }
        }
    }
}