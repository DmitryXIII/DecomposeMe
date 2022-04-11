package com.ineedyourcode.decomposeme.ui.login

class LoginActivityContract {
    interface LoginView {
        fun setLoginSuccess(login: String)
        fun setAdminLoginSuccess()
        fun setLoginError(error: String)
        fun setLogout()
        fun showUserList(userList: String)
        fun showRemindedPassword(remindedPassword: String)
        fun showProgress()
        fun hideProgress()
    }

    interface LoginPresenter {
        fun onAttach(mView: LoginView)
        fun onLogin(login: String, password: String)
        fun onLogout()
        fun onPasswordRemind(login: String)
        fun getUserList()
    }
}