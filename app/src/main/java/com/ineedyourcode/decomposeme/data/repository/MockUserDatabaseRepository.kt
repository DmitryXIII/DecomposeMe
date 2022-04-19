package com.ineedyourcode.decomposeme.data.repository

import com.ineedyourcode.decomposeme.data.db.UserDao
import com.ineedyourcode.decomposeme.data.db.UserEntity
import com.ineedyourcode.decomposeme.data.db.defaultdbbuilder.DefaultUserDbBuilder
import com.ineedyourcode.decomposeme.data.utils.fakeDelay
import com.ineedyourcode.decomposeme.domain.repository.IUserDatabaseRepository

class MockUserDatabaseRepository(
    private val roomDataSource: UserDao,
) : IUserDatabaseRepository {

    init {
        if (roomDataSource.getAllUsers().isEmpty()) {
            DefaultUserDbBuilder(roomDataSource).apply {
                initDefaultUserDataBase()
            }
        }
    }

    override fun addUser(login: String, password: String, callback: (Int) -> Unit) {
        Thread {
            Thread.sleep(fakeDelay())
            callback(
                if (roomDataSource.getUser(login) == null) {
                    roomDataSource.createUser(
                        UserEntity(
                            userLogin = login,
                            userPassword = password
                        )
                    )
                    RepositoryResponseCodes.RESPONSE_SUCCESS.code
                } else {
                    RepositoryResponseCodes.RESPONSE_LOGIN_REGISTERED_YET.code
                }
            )
        }.start()
    }

    override fun getUser(login: String, callback: (UserEntity?) -> Unit) {
        Thread {
            Thread.sleep(fakeDelay())
            callback(roomDataSource.getUser(login))
        }.start()
    }

    override fun getAllUsers(callback: (List<UserEntity>) -> Unit) {
        Thread {
            Thread.sleep(fakeDelay())
            callback(roomDataSource.getAllUsers())
        }.start()
    }

    override fun updateUser(
        userId: Int,
        newLogin: String,
        newPassword: String,
        isAuthorized: Boolean,
        callback: (Int) -> Unit,
    ) {
        Thread {
            Thread.sleep(fakeDelay())
            roomDataSource.updateUser(userId, newLogin, newPassword, isAuthorized)
            callback(
                when (roomDataSource.getUser(newLogin)) {
                    null -> {
                        RepositoryResponseCodes.RESPONSE_USER_UPDATE_FAILED.code
                    }
                    else -> {
                        RepositoryResponseCodes.RESPONSE_SUCCESS.code
                    }
                }
            )
        }.start()
    }

    override fun deleteUser(login: String, callback: (Int) -> Unit) {
        Thread {
            Thread.sleep(fakeDelay())
            callback(
                when (roomDataSource.getUser(login)) {
                    null -> {
                        RepositoryResponseCodes.RESPONSE_LOGIN_NOT_REGISTERED.code
                    }
                    else -> {
                        roomDataSource.deleteUser(login)
                        when (roomDataSource.getUser(login)) {
                            null -> {
                                RepositoryResponseCodes.RESPONSE_SUCCESS.code
                            }
                            else -> {
                                RepositoryResponseCodes.RESPONSE_USER_DELETE_FAILED.code
                            }
                        }
                    }
                }
            )
        }.start()
    }
}

private enum class RepositoryResponseCodes(val code: Int) {
    RESPONSE_SUCCESS(200),
    RESPONSE_LOGIN_NOT_REGISTERED(404),
    RESPONSE_LOGIN_REGISTERED_YET(444),
    RESPONSE_USER_UPDATE_FAILED(454),
    RESPONSE_USER_DELETE_FAILED(464)
}