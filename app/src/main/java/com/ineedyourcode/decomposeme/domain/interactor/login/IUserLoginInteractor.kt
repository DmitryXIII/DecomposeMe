package com.ineedyourcode.decomposeme.domain.interactor.login

import com.ineedyourcode.decomposeme.domain.db.UserEntity

interface IUserLoginInteractor {
    fun login(login: String, password: String, callback: (Int) -> Unit)
    fun logout(login: String, callback: (Int) -> Unit)
    fun remindUserPassword(login: String, callback: (String) -> Unit)
    fun getAllUsers(callback: (List<UserEntity>) -> Unit)
}