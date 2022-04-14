package com.ineedyourcode.decomposeme.ui.login

import android.os.Handler
import android.os.Looper
import com.ineedyourcode.decomposeme.App
import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.*
import com.ineedyourcode.decomposeme.domain.repository.IUserRepository
import com.ineedyourcode.decomposeme.domain.repository.UserRepository

class LoginActivityPresenter : LoginActivityContract.LoginPresenter {
    private val userRepository: IUserRepository = UserRepository(App.getUserDao())
    private var isLoginSuccess = false
    private val uiThread = Handler(Looper.getMainLooper())
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
        if (login.isBlank()) {
            view.setLoginError((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            uiThread.postDelayed({
                when (userRepository.checkUser(login, password)) {
                    REQUEST_CODE_OK -> {
                        view.hideProgress()
                        view.setLoginSuccess(login)
                        if (login == ADMIN_LOGIN) {
                            view.setAdminLoginSuccess()
                        }
                        isLoginSuccess = true
                        currentLogin = login
                    }

                    REQUEST_CODE_LOGIN_NOT_REGISTERED -> {
                        view.hideProgress()
                        view.setLoginError(
                            (view as LoginActivity).getString(
                                R.string.login_not_registered,
                                login
                            )
                        )
                    }

                    REQUEST_CODE_INVALID_PASSWORD -> {
                        view.hideProgress()
                        view.setLoginError((view as LoginActivity).getString(R.string.invalid_password))
                    }
                }
            }, fakeDelay())
        }
    }

    override fun onAccountExit() {
        view.showProgress()
        uiThread.postDelayed({
            view.exitAccount()
            isLoginSuccess = false
            currentLogin = (view as LoginActivity).getString(R.string.empty_text)
            view.hideProgress()
        }, fakeDelay())
    }

    override fun onPasswordRemind(login: String) {
        if (login.isBlank()) {
            view.setLoginError((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            uiThread.postDelayed({
                view.showRemindedPassword(userRepository.remindUserPassword(login))
                view.hideProgress()
            }, fakeDelay())
        }
    }

    override fun getUserList() {
        view.showProgress()
        uiThread.postDelayed({
            val mUserList = userRepository.getAllUsers()

            if (mUserList.isNotEmpty()) {
                val userList = StringBuilder()
                for (user in mUserList) {
                    userList.append(user.userLogin)
                    userList.append(" : ")
                    userList.append(user.userPassword)
                    userList.append("\n")
                }
                view.showUserList(userList.toString())
                view.hideProgress()
            } else {
                view.showUserList((view as LoginActivity).getString(R.string.empty_text))
                view.hideProgress()
            }
        }, fakeDelay())
    }
}