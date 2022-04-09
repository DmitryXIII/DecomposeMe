package com.ineedyourcode.decomposeme.domain.contracts

class LoginActivityContract {
    interface LoginView {
        fun setLoginSuccess(login: String)
        fun setLoginError(error: String)
        fun showRemindedPassword(remindedPassword: String)
        fun showProgress()
        fun hideProgress()
    }

    interface LoginPresenter {
        fun onAttach(mView: LoginActivityContract.LoginView)
        fun onLogin(login: String, password: String)
        fun onRegister()
        fun onPasswordRemind(login: String)
    }
}