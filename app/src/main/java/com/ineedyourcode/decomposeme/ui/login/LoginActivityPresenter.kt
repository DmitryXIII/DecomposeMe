package com.ineedyourcode.decomposeme.ui.login

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.data.ADMIN_LOGIN
import com.ineedyourcode.decomposeme.data.REQUEST_CODE_INVALID_PASSWORD
import com.ineedyourcode.decomposeme.data.REQUEST_CODE_LOGIN_NOT_REGISTERED
import com.ineedyourcode.decomposeme.data.REQUEST_CODE_OK
import com.ineedyourcode.decomposeme.domain.interactor.login.IUserLoginInteractor
import com.ineedyourcode.decomposeme.domain.interactor.remindpassword.IRemindPasswordInteractor
import com.ineedyourcode.decomposeme.domain.repository.IUserDatabaseRepository

class LoginActivityPresenter(
    private val userRepository: IUserDatabaseRepository,
    private val userLoginInteractor: IUserLoginInteractor,
    private val userRemindPasswordInteractor: IRemindPasswordInteractor
) :
    LoginActivityContract.LoginPresenter {
    private var isLoginSuccess = false
    private lateinit var currentLogin: String
    private lateinit var view: LoginActivityContract.LoginView

    override fun onAttach(mView: LoginActivityContract.LoginView) {
        view = mView

        userRepository.getAllUsers { userList ->
            for (user in userList) {
                if (user.isAuthorized) {
                    isLoginSuccess = true
                    currentLogin = user.userLogin
                }
            }
        }

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
            userLoginInteractor.login(login, password) { response ->
                when (response) {
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
            }
        }
    }

    override fun onLogout() {
        view.showProgress()
        userLoginInteractor.logout(currentLogin) { response ->
            when (response) {
                REQUEST_CODE_LOGIN_NOT_REGISTERED -> {
                    view.hideProgress()
                    view.setLoginError(
                        (view as LoginActivity).getString(
                            R.string.login_not_registered,
                            currentLogin
                        )
                    )
                }

                REQUEST_CODE_OK -> {
                    view.setLogout()
                    isLoginSuccess = false
                    currentLogin = (view as LoginActivity).getString(R.string.empty_text)
                    view.hideProgress()
                }
            }
        }
    }

    override fun onPasswordRemind(login: String) {
        if (login.isBlank()) {
            view.setLoginError((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            userRemindPasswordInteractor.remindUserPassword(login) { response ->
                view.showRemindedPassword(response)
                view.hideProgress()
            }
        }
    }

    override fun getUserList() {
        view.showProgress()

        userRepository.getAllUsers { mUserList ->
            if (mUserList.isNotEmpty()) {
                val userList = StringBuilder()
                for (user in mUserList) {
                    userList.append(user.userLogin)
                    userList.append(" : ")
                    userList.append(user.userPassword)
                    userList.append(" : ")
                    userList.append(user.isAuthorized)
                    userList.append("\n")
                }
                view.showUserList(userList.toString())
            } else {
                view.showUserList((view as LoginActivity).getString(R.string.empty_text))
            }
            view.hideProgress()
        }
    }
}