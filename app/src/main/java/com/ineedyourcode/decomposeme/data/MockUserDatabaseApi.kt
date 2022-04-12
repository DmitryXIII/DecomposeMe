package com.ineedyourcode.decomposeme.data

import com.ineedyourcode.decomposeme.domain.IUserDatabaseApi
import com.ineedyourcode.decomposeme.domain.db.UserDao
import com.ineedyourcode.decomposeme.domain.db.UserEntity
import java.util.*

class MockUserDatabaseApi(private val roomDataSource: UserDao) : IUserDatabaseApi {

    init {
        if (roomDataSource.getAllUsers().isEmpty()) {
            initDefaultUserDataBase()
        }
    }

    private fun initDefaultUserDataBase() {
        for (i in 0..9) {
            when (i) {
                0 -> {
                    roomDataSource.insertNewUser(
                        UserEntity(
                            UUID.randomUUID().toString(),
                            ADMIN_LOGIN,
                            ADMIN_PASSWORD
                        )
                    )
                }

                in 1..9 -> {
                    roomDataSource.insertNewUser(
                        UserEntity(
                            UUID.randomUUID().toString(),
                            "User_$i",
                            "pass$i"
                        )
                    )
                }
            }
        }
    }

    override fun getUser(login: String): UserEntity? {
        Thread.sleep(fakeDelay())
        return roomDataSource.getUser(login)
    }

    override fun login(login: String, password: String): Int {
        Thread.sleep(fakeDelay())
        val user = roomDataSource.getUser(login)
        return when {
            user == null -> {
                REQUEST_CODE_LOGIN_NOT_REGISTERED
            }
            user.userPassword != password -> {
                REQUEST_CODE_INVALID_PASSWORD
            }
            else -> {
                roomDataSource.userLogin(login)
                REQUEST_CODE_OK
            }
        }
    }

    override fun logout(login: String): Int {
        Thread.sleep(fakeDelay())
        return when (roomDataSource.getUser(login)) {
            null -> {
                REQUEST_CODE_LOGIN_NOT_REGISTERED
            }
            else -> {
                roomDataSource.userLogout(login)
                REQUEST_CODE_OK
            }
        }
    }

    override fun remindUserPassword(login: String) : String {
        Thread.sleep(fakeDelay())
        return if (roomDataSource.getUser(login)?.userPassword == null) {
            "Логин \"${login}\" не зарегистрирован"
        } else {
            "Пароль: ${roomDataSource.getUser(login)?.userPassword}"
        }
    }

    override fun addNewUser(login: String, password: String): Int {
        Thread.sleep(fakeDelay())
        return if (getUser(login) == null) {
            roomDataSource.insertNewUser(UserEntity(UUID.randomUUID().toString(), login, password, false))
            REQUEST_CODE_OK
        } else {
            REQUEST_CODE_LOGIN_REGISTERED_YET
        }
    }

    override fun getAllUsers(): List<UserEntity> {
        Thread.sleep(fakeDelay())
        return roomDataSource.getAllUsers()
    }

    override fun deleteUser(login: String): Int {
        Thread.sleep(fakeDelay())
        return if (getUser(login) == null) {
            REQUEST_CODE_LOGIN_NOT_REGISTERED
        } else {
            roomDataSource.deleteUser(login)
            REQUEST_CODE_OK
        }
    }
}