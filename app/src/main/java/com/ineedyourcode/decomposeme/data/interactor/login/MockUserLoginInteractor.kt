package com.ineedyourcode.decomposeme.data.interactor.login

import com.ineedyourcode.decomposeme.domain.api.IUserDatabaseApi
import com.ineedyourcode.decomposeme.domain.interactor.login.IUserLoginInteractor

class MockUserLoginInteractor(
    private val userDataBaseApi: IUserDatabaseApi,
) : IUserLoginInteractor {
    override fun login(login: String, password: String, callback: (Int) -> Unit) {
        Thread {
            callback(userDataBaseApi.login(login, password))
        }.start()
    }

    override fun logout(login: String?, callback: (Int) -> Unit) {
        Thread {
            callback(userDataBaseApi.logout(login))
        }.start()
    }
}