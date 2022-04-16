package com.ineedyourcode.decomposeme.data.repository

import android.os.Handler
import com.ineedyourcode.decomposeme.data.db.UserDao
import com.ineedyourcode.decomposeme.data.db.UserEntity
import com.ineedyourcode.decomposeme.data.db.defaultdbbuilder.DefaultUserDbBuilder
import com.ineedyourcode.decomposeme.data.utils.MockDatabaseConstants
import com.ineedyourcode.decomposeme.data.utils.fakeDelay
import com.ineedyourcode.decomposeme.domain.repository.IUserDatabaseRepository

class MockUserDatabaseRepository(
    private val roomDataSource: UserDao,
    private val uiHandler: Handler,
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
            uiHandler.post {
                callback(
                    if (roomDataSource.getUser(login) == null) {
                        roomDataSource.createUser(
                            UserEntity(
                                userLogin = login,
                                userPassword = password
                            )
                        )
                        MockDatabaseConstants.ResponseCodes.RESPONSE_SUCCESS.code
                    } else {
                        MockDatabaseConstants.ResponseCodes.RESPONSE_LOGIN_REGISTERED_YET.code
                    }
                )
            }
        }.start()
    }

    override fun getUser(login: String, callback: (UserEntity?) -> Unit) {
        Thread {
            Thread.sleep(fakeDelay())
            uiHandler.post {
                callback(roomDataSource.getUser(login))
            }
        }.start()
    }

    override fun getAllUsers(callback: (List<UserEntity>) -> Unit) {
        Thread {
            Thread.sleep(fakeDelay())
            uiHandler.post {
                callback(roomDataSource.getAllUsers())
            }
        }.start()
    }

    override fun updateUser(
        userId: String,
        newLogin: String,
        newPassword: String,
        isAuthorized: Boolean,
        callback: (Int) -> Unit,
    ) {
        Thread {
            Thread.sleep(fakeDelay())
            roomDataSource.updateUser(userId, newLogin, newPassword, isAuthorized)
            uiHandler.post {
                callback(
                    when (roomDataSource.getUser(newLogin)) {
                        null -> {
                            MockDatabaseConstants.ResponseCodes.RESPONSE_USER_UPDATE_FAILED.code
                        }
                        else -> {
                            MockDatabaseConstants.ResponseCodes.RESPONSE_SUCCESS.code
                        }
                    }
                )
            }
        }.start()
    }

    override fun deleteUser(login: String, callback: (Int) -> Unit) {
        Thread {
            Thread.sleep(fakeDelay())
            uiHandler.post {
                callback(
                    when (roomDataSource.getUser(login)) {
                        null -> {
                            MockDatabaseConstants.ResponseCodes.RESPONSE_LOGIN_NOT_REGISTERED.code
                        }
                        else -> {
                            roomDataSource.deleteUser(login)
                            when (roomDataSource.getUser(login)) {
                                null -> {
                                    MockDatabaseConstants.ResponseCodes.RESPONSE_SUCCESS.code
                                }
                                else -> {
                                    MockDatabaseConstants.ResponseCodes.RESPONSE_USER_DELETE_FAILED.code
                                }
                            }
                        }
                    }
                )
            }
        }.start()
    }
}