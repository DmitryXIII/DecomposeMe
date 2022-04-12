package com.ineedyourcode.decomposeme.data

import android.os.Handler
import com.ineedyourcode.decomposeme.domain.IUserDatabaseApi
import com.ineedyourcode.decomposeme.domain.interactor.registration.IUserRegistrationInteractor

class MockUserRegistrationInteractor(
    private val userDatabaseApi: IUserDatabaseApi,
    private val uiHandler: Handler
) : IUserRegistrationInteractor {
    override fun addNewUser(login: String, password: String, callback: (Int) -> Unit) {
        Thread {
            uiHandler.post {
                callback(userDatabaseApi.addNewUser(login, password))
            }
        }.start()
    }
}