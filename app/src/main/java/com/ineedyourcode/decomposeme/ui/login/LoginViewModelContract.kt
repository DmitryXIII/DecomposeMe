package com.ineedyourcode.decomposeme.ui.login

import com.ineedyourcode.decomposeme.data.db.UserEntity
import com.ineedyourcode.decomposeme.ui.utils.Publisher

interface LoginViewModelContract {
    val showProgress: Publisher<Boolean>
    val isLoginSuccess: Publisher<String>
    val isLogout: Publisher<Boolean>
    val receivedUser: Publisher<UserEntity>
    val receivedUserList: Publisher<String>
    val messenger: Publisher<Pair<Int, Any?>>

    fun onCheckOnAppStartAuthorization()
    fun onDeleteUser(login: String)
    fun onGetUser(login: String)
    fun onGetUserList()
    fun onLogin(login: String, password: String)
    fun onLogout()
    fun onPasswordRemind(login: String)
    fun onUpdateUser(userId: String, login: String, password: String)
}