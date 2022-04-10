package com.ineedyourcode.decomposeme.presenter.login

import com.ineedyourcode.decomposeme.domain.UserDto

class LoginActivityContract {
    interface LoginView {
        fun setLoginSuccess(login: String)
        fun setAdminLoginSuccess()
        fun setLoginError(error: String)
        fun exitAccount()
        fun showRemindedPassword(remindedPassword: String)
        fun showProgress()
        fun hideProgress()
    }

    interface LoginPresenter {
        fun onAttach(mView: LoginView)
        fun onLogin(login: String, password: String)
        fun onAccountExit()
        fun onPasswordRemind(login: String)
        fun getUserList(): List<UserDto>
    }
}