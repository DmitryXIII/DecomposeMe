package com.ineedyourcode.decomposeme.domain.interactor.login

interface IUserLoginInteractor {
    fun login(login: String, password: String, callback: (Int) -> Unit)
    fun logout(login: String?, callback: (Int) -> Unit)
}