package com.ineedyourcode.decomposeme.data.api

import com.ineedyourcode.decomposeme.data.db.UserDao
import com.ineedyourcode.decomposeme.data.utils.fakeDelay
import com.ineedyourcode.decomposeme.domain.api.IUserDatabaseApi

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

    override fun logout(login: String?): Int {
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

    override fun remindUserPassword(login: String): Any {
        Thread.sleep(fakeDelay())
        return roomDataSource.getUser(login)?.userPassword
            ?: ApiResponseCodes.RESPONSE_LOGIN_NOT_REGISTERED.code
    }
}

private enum class ApiResponseCodes(val code: Int) {
    RESPONSE_SUCCESS(200),
    RESPONSE_INVALID_PASSWORD(403),
    RESPONSE_LOGIN_NOT_REGISTERED(404)
}