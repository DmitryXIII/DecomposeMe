package com.ineedyourcode.decomposeme.data.repository

import android.os.Handler
import com.ineedyourcode.decomposeme.data.*
import com.ineedyourcode.decomposeme.data.db.UserDao
import com.ineedyourcode.decomposeme.data.db.UserEntity
import com.ineedyourcode.decomposeme.domain.repository.IUserDatabaseRepository

class MockUserDatabaseRepository(
    private val roomDataSource: UserDao,
    private val uiHandler: Handler
) : IUserDatabaseRepository {

    override fun addUser(login: String, password: String, callback: (Int) -> Unit) {
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
                    REQUEST_CODE_OK
                } else {
                    REQUEST_CODE_LOGIN_REGISTERED_YET
                }
            )
        }
    }

    override fun getUser(login: String, callback: (UserEntity?) -> Unit) {
        Thread.sleep(fakeDelay())

        uiHandler.post {
            callback(roomDataSource.getUser(login))
        }
    }

    override fun getAllUsers(callback: (List<UserEntity>) -> Unit) {
        Thread.sleep(fakeDelay())
        uiHandler.post {
            callback(roomDataSource.getAllUsers())
        }
    }

    override fun updateUser(
        userId: String,
        newLogin: String,
        newPassword: String,
        isAuthorized: Boolean,
        callback: (Int) -> Unit
    ) {
        Thread.sleep(fakeDelay())

        roomDataSource.updateUser(userId, newLogin, newPassword, isAuthorized)

        uiHandler.post {
            callback(
                when (roomDataSource.getUser(newLogin)) {
                    null -> {
                        REQUEST_CODE_USER_UPDATE_FAILED
                    }
                    else -> {
                        REQUEST_CODE_OK
                    }
                }
            )
        }
    }

    override fun deleteUser(login: String, callback: (Int) -> Unit) {
        Thread.sleep(fakeDelay())

        uiHandler.post {
            callback(
                when (roomDataSource.getUser(login)) {
                    null -> {
                        REQUEST_CODE_LOGIN_NOT_REGISTERED
                    }
                    else -> {
                        roomDataSource.deleteUser(login)
                        when (roomDataSource.getUser(login)) {
                            null -> {
                                REQUEST_CODE_OK
                            }
                            else -> {
                                REQUEST_CODE_USER_DELETE_FAILED
                            }
                        }
                    }
                }
            )
        }
    }
}