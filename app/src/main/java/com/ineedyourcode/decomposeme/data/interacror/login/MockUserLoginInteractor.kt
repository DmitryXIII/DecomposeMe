package com.ineedyourcode.decomposeme.data.interacror.login

import android.os.Handler
import com.ineedyourcode.decomposeme.domain.api.IUserDatabaseApi
import com.ineedyourcode.decomposeme.domain.interactor.login.IUserLoginInteractor

class MockUserLoginInteractor(
    private val userDataBaseApi: IUserDatabaseApi,
    private val uiHandler: Handler
) : IUserLoginInteractor {
    override fun login(login: String, password: String, callback: (Int) -> Unit) {
        Thread {
            uiHandler.post {
                callback(userDataBaseApi.login(login, password))
            }
        }.start()
    }

    override fun logout(login: String, callback: (Int) -> Unit) {
        Thread {
            uiHandler.post {
                callback(userDataBaseApi.logout(login))
            }
        }.start()
    }
}