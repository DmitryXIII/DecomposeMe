package com.ineedyourcode.decomposeme.ui.login

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.interactor.login.IUserLoginInteractor
import com.ineedyourcode.decomposeme.domain.interactor.remindpassword.IRemindPasswordInteractor
import com.ineedyourcode.decomposeme.domain.repository.IUserDatabaseRepository
import com.ineedyourcode.decomposeme.ui.resourses.DefaultUsersParams
import com.ineedyourcode.decomposeme.ui.resourses.ResponseCodes

class LoginActivityPresenter(
    private val userRepository: IUserDatabaseRepository,
    private val userLoginInteractor: IUserLoginInteractor,
    private val userRemindPasswordInteractor: IRemindPasswordInteractor,
    private var isFirstAttach: Boolean
) :
    LoginActivityContract.LoginPresenter {

    private var isLoginSuccess = false
    private lateinit var currentLogin: String
    private lateinit var view: LoginActivityContract.LoginView


    // Задача onAttach: при открытии приложения реализовать проверку - есть ли авторизованный пользователь.
    // флаг isFirstAttach - для проверки, активити создается при открытии приложения, или активити восстановлена
    // после поворота экрана и т.д.
    override fun onAttach(mView: LoginActivityContract.LoginView) {
        view = mView

        if (!isFirstAttach) {
            if (isLoginSuccess) {
                view.setLoginSuccess(currentLogin)
                if (currentLogin == DefaultUsersParams.DEFAULT_ADMIN_LOGIN.value) {
                    view.setAdminLoginSuccess()
                }
            }
        } else {
            view.showProgress()
            userRepository.getAllUsers { userList ->
                for (user in userList) {
                    if (user.isAuthorized) {
                        isLoginSuccess = true
                        currentLogin = user.userLogin
                        break
                    }
                }
                if (isLoginSuccess) {
                    view.setLoginSuccess(currentLogin)
                    if (currentLogin == DefaultUsersParams.DEFAULT_ADMIN_LOGIN.value) {
                        view.setAdminLoginSuccess()
                    }
                }
                view.hideProgress()
            }
            isFirstAttach = false
        }
    }

    override fun onLogin(login: String, password: String) {
        if (login.isBlank()) {
            view.setLoginError((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            userLoginInteractor.login(login, password) { response ->
                when (response) {
                    ResponseCodes.RESPONSE_SUCCESS.code -> {
                        view.hideProgress()
                        view.setLoginSuccess(login)
                        if (login == DefaultUsersParams.DEFAULT_ADMIN_LOGIN.value) {
                            view.setAdminLoginSuccess()
                        }
                        isLoginSuccess = true
                        currentLogin = login
                    }

                    ResponseCodes.RESPONSE_LOGIN_NOT_REGISTERED.code -> {
                        view.hideProgress()
                        view.setLoginError(
                            (view as LoginActivity).getString(
                                R.string.login_not_registered,
                                login
                            )
                        )
                    }

                    ResponseCodes.RESPONSE_INVALID_PASSWORD.code -> {
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
                ResponseCodes.RESPONSE_LOGIN_NOT_REGISTERED.code -> {
                    view.hideProgress()
                    view.setLoginError(
                        (view as LoginActivity).getString(
                            R.string.login_not_registered,
                            currentLogin
                        )
                    )
                }

                ResponseCodes.RESPONSE_SUCCESS.code -> {
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