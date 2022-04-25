package com.ineedyourcode.decomposeme.data.interactor.remindpassword

import com.ineedyourcode.decomposeme.domain.api.IUserDatabaseApi
import com.ineedyourcode.decomposeme.domain.interactor.remindpassword.IRemindPasswordInteractor

class MockRemindPasswordInteractor(
    private val userDatabaseApi: IUserDatabaseApi,
) : IRemindPasswordInteractor {
    override fun remindUserPassword(login: String, callback: (Any) -> Unit) {
        Thread {
            callback(userDatabaseApi.remindUserPassword(login))
        }.start()
    }
}