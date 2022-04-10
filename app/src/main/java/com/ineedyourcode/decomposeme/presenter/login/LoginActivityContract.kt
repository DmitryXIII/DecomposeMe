package com.ineedyourcode.decomposeme.presenter.login

import com.ineedyourcode.decomposeme.domain.UserDto

class LoginActivityContract {
    interface LoginView {
        fun setLoginSuccess(login: String)
        fun setLoginError(error: String)
        fun showRemindedPassword(remindedPassword: String)
        fun showProgress()
        fun hideProgress()
    }

    interface LoginPresenter {
        fun onAttach(mView: LoginView)
        fun onLogin(login: String, password: String)
        fun onRegister()
        fun onPasswordRemind(login: String)
        fun getUserList() : Map<String, UserDto>
        fun delUser(login: String)
    }
}