package com.ineedyourcode.decomposeme.presenter.login

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.*
import com.ineedyourcode.decomposeme.domain.repository.IUserRepository
import com.ineedyourcode.decomposeme.domain.repository.UserRepository
import com.ineedyourcode.decomposeme.ui.login.LoginActivity

class LoginActivityPresenter : LoginActivityContract.LoginPresenter {
    private val userRepository: IUserRepository = UserRepository(App.getUserDao())

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
                REQUEST_CODE_OK -> {
                    view.setLoginSuccess(login)
                }

                REQUEST_CODE_LOGIN_NOT_REGISTERED -> {
                    view.setLoginError(
                        (view as LoginActivity).getString(R.string.login_not_registered, login)
                    )
                }

                REQUEST_CODE_INVALID_PASSWORD -> {
                    view.setLoginError((view as LoginActivity).getString(R.string.invalid_password))
                }
            }
        }
    }

    override fun onRegister() {
        // TODO("Not yet implemented")
    }

    override fun onPasswordRemind(login: String) {
        if (login.isBlank()) {
            view.setLoginError((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showRemindedPassword(userRepository.remindUserPassword(login))
        }
    }

    override fun getUserList() : Map<String, UserDto> {
       return userRepository.getAllUsers()
    }

    override fun delUser(login: String) {
        userRepository.deleteUser(login)
    }
}