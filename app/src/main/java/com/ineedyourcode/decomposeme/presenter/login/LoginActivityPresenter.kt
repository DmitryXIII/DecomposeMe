package com.ineedyourcode.decomposeme.presenter.login

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.*
import com.ineedyourcode.decomposeme.domain.repository.IUserRepository
import com.ineedyourcode.decomposeme.domain.repository.UserRepository
import com.ineedyourcode.decomposeme.ui.login.LoginActivity

class LoginActivityPresenter : LoginActivityContract.LoginPresenter {
    private val userRepository: IUserRepository = UserRepository(App.getUserDao())
    private var isLoginSuccess = false
    private lateinit var currentLogin: String
    private lateinit var view: LoginActivityContract.LoginView

    override fun onAttach(mView: LoginActivityContract.LoginView) {
        view = mView
        if (isLoginSuccess) {
            view.setLoginSuccess(currentLogin)
            if (currentLogin == ADMIN_LOGIN) {
                view.setAdminLoginSuccess()
            }
        }
    }

    override fun onLogin(login: String, password: String) {
        view.hideProgress()
        if (login.isBlank()) {
            view.setLoginError((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            when (userRepository.checkUser(login, password)) {
                REQUEST_CODE_OK -> {
                    view.setLoginSuccess(login)
                    if (login == ADMIN_LOGIN) {
                        view.setAdminLoginSuccess()
                    }
                    isLoginSuccess = true
                    currentLogin = login
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

    override fun onAccountExit() {
        view.exitAccount()
        isLoginSuccess = false
        currentLogin = (view as LoginActivity).getString(R.string.empty_text)
    }

    override fun onPasswordRemind(login: String) {
        if (login.isBlank()) {
            view.setLoginError((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showRemindedPassword(userRepository.remindUserPassword(login))
        }
    }

    override fun getUserList(): List<UserDto> {
        return userRepository.getAllUsers()
    }
}