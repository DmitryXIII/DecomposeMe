package com.ineedyourcode.decomposeme.presenter

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.contracts.LoginActivityContract
import com.ineedyourcode.decomposeme.domain.fakerepository.FakeUserRepository
import com.ineedyourcode.decomposeme.domain.fakerepository.IFakeUserRepository
import com.ineedyourcode.decomposeme.ui.LoginActivity

class LoginActivityPresenter : LoginActivityContract.LoginPresenter {
    private val userRepository: IFakeUserRepository = FakeUserRepository()

    private lateinit var view: LoginActivityContract.LoginView

    override fun onAttach(mView: LoginActivityContract.LoginView) {
        view = mView
    }

    override fun onLogin(login: String, password: String) {
        view.hideProgress()
        if (login.isBlank()) {
            view.setLoginError((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            when (userRepository.checkUser(login, password)) {
                200 -> {
                    view.setLoginSuccess(login)
                }

                401 -> {
                    view.setLoginError(
                        (view as LoginActivity).getString(R.string.login_not_registered, login)
                    )
                }

                403 -> {
                    view.setLoginError((view as LoginActivity).getString(R.string.invalid_password))
                }
            }
        }
    }

    override fun onRegister() {
        // TODO("Not yet implemented")
    }

    override fun onPasswordRemind(login: String) {
        view.showRemindedPassword(userRepository.remindUserPassword(login))
    }
}