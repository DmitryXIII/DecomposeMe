package com.ineedyourcode.decomposeme.domain.interactor.remindpassword

interface IRemindPasswordInteractor {
    fun remindUserPassword(login: String, callback: (Any) -> Unit)
}