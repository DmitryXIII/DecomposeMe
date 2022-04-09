package com.ineedyourcode.decomposeme.domain

class LoginActivityContract {
    interface LoginView {
        fun setLoginSuccess()
        fun setLoginError()
        fun showRestoredPassword()
        fun showProgress()
        fun hideProgress()
    }

    interface LoginPresenter {
        fun onAttach()
        fun onLogin()
        fun onRegister()
        fun onPasswordRestore()
    }
}