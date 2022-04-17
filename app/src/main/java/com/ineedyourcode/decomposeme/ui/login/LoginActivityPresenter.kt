package com.ineedyourcode.decomposeme.ui.login

import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.interactor.login.IUserLoginInteractor
import com.ineedyourcode.decomposeme.domain.interactor.remindpassword.IRemindPasswordInteractor
import com.ineedyourcode.decomposeme.domain.repository.IUserDatabaseRepository

private const val DEFAULT_ADMIN_LOGIN = "admin"

private enum class ResponseCodes(val code: Int) {
    RESPONSE_SUCCESS(200),
    RESPONSE_INVALID_PASSWORD(403),
    RESPONSE_LOGIN_NOT_REGISTERED(404),
    RESPONSE_USER_UPDATE_FAILED(454),
    RESPONSE_USER_DELETE_FAILED(464)
}

class LoginActivityPresenter(
    private val userRepository: IUserDatabaseRepository,
    private val userLoginInteractor: IUserLoginInteractor,
    private val userRemindPasswordInteractor: IRemindPasswordInteractor,
    private var isFirstAttach: Boolean,
) :
    LoginActivityContract.LoginPresenter {

    private var isLoginSuccess = false
    private lateinit var currentLogin: String
    private lateinit var view: LoginActivityContract.LoginView

    override fun onAttach(mView: LoginActivityContract.LoginView) {
        view = mView

        if (!isFirstAttach) {
            checkIsLoginSuccess()
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
                checkIsLoginSuccess()
                view.hideProgress()
            }
            isFirstAttach = false
        }
    }

    private fun checkIsLoginSuccess() {
        if (isLoginSuccess) {
            view.setLoginSuccess(currentLogin)
            if (currentLogin == DEFAULT_ADMIN_LOGIN) {
                view.setAdminLoginSuccess()
            }
        }
    }

    override fun onLogin(login: String, password: String) {
        if (login.isBlank()) {
            view.showMessage((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            userLoginInteractor.login(login, password) { response ->
                when (response) {
                    ResponseCodes.RESPONSE_SUCCESS.code -> {
                        view.hideProgress()
                        view.setLoginSuccess(login)
                        if (login == DEFAULT_ADMIN_LOGIN) {
                            view.setAdminLoginSuccess()
                        }
                        isLoginSuccess = true
                        currentLogin = login
                    }

                    ResponseCodes.RESPONSE_LOGIN_NOT_REGISTERED.code -> {
                        view.hideProgress()
                        view.showMessage(
                            (view as LoginActivity).getString(
                                R.string.login_not_registered,
                                login
                            )
                        )
                    }

                    ResponseCodes.RESPONSE_INVALID_PASSWORD.code -> {
                        view.hideProgress()
                        view.showMessage((view as LoginActivity).getString(R.string.invalid_password))
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
                    view.showMessage(
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
            view.showMessage((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            userRemindPasswordInteractor.remindUserPassword(login) { response ->
                view.showRemindedPassword(response)
                view.hideProgress()
            }
        }
    }

    override fun onGetUser(login: String) {
        if (login.isBlank()) {
            view.showMessage((view as LoginActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            userRepository.getUser(login) { user ->
                if (user == null) {
                    view.showMessage(
                        (view as LoginActivity).getString(
                            R.string.login_not_registered,
                            login
                        ))
                    view.hideProgress()
                } else {
                    view.receiveUser(user.userLogin, user.userPassword, user.userId)
                    view.hideProgress()
                }
            }
        }
    }

    override fun onGetUserList() {
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
                    userList.append("----------\n")
                }
                view.showUserList(userList.toString())
                view.hideProgress()
            } else {
                view.showUserList((view as LoginActivity).getString(R.string.empty_text))
                view.hideProgress()
            }
        }
    }

    override fun onDeleteUser(login: String) {
        when {
            login.isBlank() -> {
                view.showMessage((view as LoginActivity).getString(R.string.login_can_not_be_blank))
            }

            login == DEFAULT_ADMIN_LOGIN -> {
                view.showMessage((view as LoginActivity).getString(R.string.forbidden_to_delete_admin))
            }

            else -> {
                view.showProgress()
                userRepository.deleteUser(login) { response ->
                    when (response) {
                        ResponseCodes.RESPONSE_LOGIN_NOT_REGISTERED.code -> {
                            view.showMessage((view as LoginActivity).getString(
                                R.string.login_not_registered,
                                login
                            ))
                            view.hideProgress()
                        }
                        ResponseCodes.RESPONSE_SUCCESS.code -> {
                            onGetUserList()
                            view.showMessage((view as LoginActivity).getString(
                                R.string.login_deleted_successful,
                                login
                            ))
                            view.hideProgress()
                        }
                        ResponseCodes.RESPONSE_USER_DELETE_FAILED.code -> {
                            view.showMessage((view as LoginActivity).getString(
                                R.string.database_error
                            ))
                            view.hideProgress()
                        }
                    }
                }
            }
        }
    }

    override fun onUpdateUser(userId: Int, login: String, password: String) {
        when {
            userId.toString().isBlank() -> {
                view.showMessage((view as LoginActivity).getString(R.string.you_have_to_load_data))
            }

            login.isBlank() -> {
                view.showMessage((view as LoginActivity).getString(R.string.login_can_not_be_blank))
            }

            password.isBlank() -> {
                view.showMessage((view as LoginActivity).getString(R.string.password_can_not_be_blank))
            }

            login == DEFAULT_ADMIN_LOGIN -> {
                view.showMessage((view as LoginActivity).getString(R.string.forbidden_to_update_admin))
            }

            else -> {
                view.showProgress()
                userRepository.updateUser(userId, login, password, false) { response ->
                    when (response) {
                        ResponseCodes.RESPONSE_SUCCESS.code -> {
                            onGetUserList()
                            view.showMessage((view as LoginActivity).getString(
                                R.string.login_updated_successful
                            ))
                            view.receiveUser(login, password, userId)
                            view.hideProgress()
                        }
                        ResponseCodes.RESPONSE_USER_UPDATE_FAILED.code -> {
                            view.showMessage((view as LoginActivity).getString(
                                R.string.database_error
                            ))
                            view.hideProgress()
                        }
                    }
                }
            }
        }
    }
}