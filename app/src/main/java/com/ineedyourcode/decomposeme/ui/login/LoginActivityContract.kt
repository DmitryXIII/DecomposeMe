package com.ineedyourcode.decomposeme.ui.login

class LoginActivityContract {
    interface LoginView {
        fun hideProgress()
        fun receiveUser(login: String, password: String, id: Int)
        fun setLoginSuccess(login: String)
        fun setAdminLoginSuccess()
        fun setLogout()
        fun showMessage(message: String)
        fun showProgress()
        fun showRemindedPassword(remindedPassword: String)
        fun showUserList(userList: String)
    }

    interface LoginPresenter {
        fun onAttach(mView: LoginView)
        fun onDeleteUser(login: String)
        fun onGetUser(login: String)
        fun onGetUserList()
        fun onLogin(login: String, password: String)
        fun onLogout()
        fun onPasswordRemind(login: String)
        fun onUpdateUser(userId: Int, login: String, password: String)
    }
}