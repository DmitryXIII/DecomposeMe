package com.ineedyourcode.decomposeme.presenter.registration

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.App
import com.ineedyourcode.decomposeme.domain.repository.IUserRepository
import com.ineedyourcode.decomposeme.domain.repository.UserRepository
import com.ineedyourcode.decomposeme.ui.registration.RegistrationActivity

class RegistrationActivityPresenter : RegistrationActivityContract.RegistrationPresenter {
    private val userRepository: IUserRepository = UserRepository(App.getUserDao())

    private lateinit var view: RegistrationActivityContract.RegistrationView

    override fun onAttach(mView: RegistrationActivityContract.RegistrationView) {
        view = mView
    }

    override fun onRegister(login: String, password: String) {
        view.hideProgress()
        if (login.isBlank()) {
            view.setRegistrationError((view as RegistrationActivity).getString(R.string.login_can_not_be_blank))
        } else {
            userRepository.registerNewUser(login, password)
            view.setRegistrationSuccess(login)
        }
    }
}