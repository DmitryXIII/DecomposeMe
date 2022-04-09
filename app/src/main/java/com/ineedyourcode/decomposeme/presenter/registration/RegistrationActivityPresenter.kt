package com.ineedyourcode.decomposeme.presenter.registration

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.REQUEST_CODE_OK
import com.ineedyourcode.decomposeme.domain.REQUEST_CODE_LOGIN_REGISTERED_YET
import com.ineedyourcode.decomposeme.domain.fakerepository.FakeUserRepository
import com.ineedyourcode.decomposeme.domain.fakerepository.IFakeUserRepository
import com.ineedyourcode.decomposeme.ui.registration.RegistrationActivity

class RegistrationActivityPresenter : RegistrationActivityContract.RegistrationPresenter {
    private val userRepository: IFakeUserRepository = FakeUserRepository()

    private lateinit var view: RegistrationActivityContract.RegistrationView

    override fun onAttach(mView: RegistrationActivityContract.RegistrationView) {
        view = mView
    }

    override fun onRegister(login: String, password: String) {
        view.hideProgress()
        if (login.isBlank()) {
            view.setRegistrationError((view as RegistrationActivity).getString(R.string.login_can_not_be_blank))
        } else {
            when (userRepository.registerNewUser(login, password)) {
                REQUEST_CODE_OK -> {
                    view.setRegistrationSuccess(login)
                }

                REQUEST_CODE_LOGIN_REGISTERED_YET -> {
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