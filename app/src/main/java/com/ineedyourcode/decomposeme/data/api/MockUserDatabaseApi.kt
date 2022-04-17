package com.ineedyourcode.decomposeme.data.api

import com.ineedyourcode.decomposeme.data.db.UserDao
import com.ineedyourcode.decomposeme.data.utils.fakeDelay
import com.ineedyourcode.decomposeme.domain.api.IUserDatabaseApi

private enum class ApiResponseCodes(val code: Int) {
    RESPONSE_SUCCESS(200),
    RESPONSE_INVALID_PASSWORD(403),
    RESPONSE_LOGIN_NOT_REGISTERED(404)
}

class MockUserDatabaseApi(private val roomDataSource: UserDao) : IUserDatabaseApi {

    override fun login(login: String, password: String): Int {
        Thread.sleep(fakeDelay())
        val user = roomDataSource.getUser(login)
        return when {
            user == null -> {
                ApiResponseCodes.RESPONSE_LOGIN_NOT_REGISTERED.code
            }
            user.userPassword != password -> {
                ApiResponseCodes.RESPONSE_INVALID_PASSWORD.code
            }
            else -> {
                roomDataSource.userLogin(login)
                ApiResponseCodes.RESPONSE_SUCCESS.code
            }
        }
    }

    override fun logout(login: String): Int {
        Thread.sleep(fakeDelay())
        return when (roomDataSource.getUser(login)) {
            null -> {
                ApiResponseCodes.RESPONSE_LOGIN_NOT_REGISTERED.code
            }
            else -> {
                roomDataSource.userLogout(login)
                ApiResponseCodes.RESPONSE_SUCCESS.code
            }
        }
    }

    override fun remindUserPassword(login: String): String {
        Thread.sleep(fakeDelay())
        return if (roomDataSource.getUser(login)?.userPassword == null) {
            ApiStringResources.MESSAGE_LOGIN_NOT_REGISTERED.value
        } else {
            ApiStringResources
                .MESSAGE_YOUR_PASSWORD_IS.value + roomDataSource.getUser(login)?.userPassword
        }
    }
}

private enum class ApiStringResources(val value: String) {
    MESSAGE_LOGIN_NOT_REGISTERED("Логин не зарегистрирован"),
    MESSAGE_YOUR_PASSWORD_IS("Пароль: ")
}