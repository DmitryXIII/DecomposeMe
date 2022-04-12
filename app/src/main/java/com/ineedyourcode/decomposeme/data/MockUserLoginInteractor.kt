package com.ineedyourcode.decomposeme.data

import android.os.Handler
import com.ineedyourcode.decomposeme.domain.IUserDatabaseApi
import com.ineedyourcode.decomposeme.domain.db.UserEntity
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

    override fun remindUserPassword(login: String, callback: (String) -> Unit) {
        Thread {
            uiHandler.post {
                callback(userDataBaseApi.remindUserPassword(login))
            }
        }.start()
    }

    override fun getAllUsers(callback: (List<UserEntity>) -> Unit) {
        Thread {
            uiHandler.post {
                callback(userDataBaseApi.getAllUsers())
            }
        }.start()
    }

}