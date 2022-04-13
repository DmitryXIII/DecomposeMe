package com.ineedyourcode.decomposeme.data.interacror.remindpassword

import android.os.Handler
import com.ineedyourcode.decomposeme.domain.api.IUserDatabaseApi
import com.ineedyourcode.decomposeme.domain.interactor.remindpassword.IRemindPasswordInteractor

class MockRemindPasswordInteractor(
    private val userDatabaseApi: IUserDatabaseApi,
    private val uiHandler: Handler
) : IRemindPasswordInteractor {
    override fun remindUserPassword(login: String, callback: (String) -> Unit) {
        Thread {
            uiHandler.post {
                callback(userDatabaseApi.remindUserPassword(login))
            }
        }.start()
    }
}