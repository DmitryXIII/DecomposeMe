package com.ineedyourcode.decomposeme.domain.repository

import com.ineedyourcode.decomposeme.domain.*
import com.ineedyourcode.decomposeme.domain.db.UserDao
import com.ineedyourcode.decomposeme.domain.db.UserEntity
import java.util.*

class UserRepository(private val roomDataSource: UserDao) : IUserRepository {

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
                            ADMIN_PASSWORD,
                            false
                        )
                    )
                }

                in 1..9 -> {
                    roomDataSource.insertNewUser(
                        UserEntity(
                            UUID.randomUUID().toString(),
                            "User_$i",
                            "pass$i",
                            false
                        )
                    )
                }
            }
        }
    }

    override fun getUser(login: String): UserEntity? {
        return roomDataSource.getUser(login)
    }

    override fun login(login: String, password: String): Int {
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
        return if (roomDataSource.getUser(login)?.userPassword == null) {
            "Логин \"${login}\" не зарегистрирован"
        } else {
            "Пароль: ${roomDataSource.getUser(login)?.userPassword}"
        }
    }

    override fun addNewUser(login: String, password: String): Int {
        return if (getUser(login) == null) {
            roomDataSource.insertNewUser(UserEntity(UUID.randomUUID().toString(), login, password, false))
            REQUEST_CODE_OK
        } else {
            REQUEST_CODE_LOGIN_REGISTERED_YET
        }
    }

    override fun getAllUsers(): List<UserDto> {
        val mUserList = mutableListOf<UserDto>()

        for (mUser in roomDataSource.getAllUsers()) {
            val user = UserDto(mUser.userId, mUser.userLogin, mUser.userPassword, mUser.isAuthorized)
            mUserList.add(user)
        }
        return mUserList
    }

    override fun deleteUser(login: String): Int {
        return if (getUser(login) == null) {
            REQUEST_CODE_LOGIN_NOT_REGISTERED
        } else {
            roomDataSource.deleteUser(login)
            REQUEST_CODE_OK
        }
    }
}