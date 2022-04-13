package com.ineedyourcode.decomposeme.domain.interactor.login

import com.ineedyourcode.decomposeme.data.db.UserEntity

interface IUserLoginInteractor {
    fun login(login: String, password: String, callback: (Int) -> Unit)
    fun logout(login: String, callback: (Int) -> Unit)
}