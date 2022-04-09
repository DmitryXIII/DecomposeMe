package com.ineedyourcode.decomposeme.domain.fakerepository

import com.ineedyourcode.decomposeme.domain.REQUEST_CODE_INVALID_PASSWORD
import com.ineedyourcode.decomposeme.domain.REQUEST_CODE_LOGIN_NOT_REGISTERED
import com.ineedyourcode.decomposeme.domain.REQUEST_CODE_LOGIN_REGISTERED_YET
import com.ineedyourcode.decomposeme.domain.REQUEST_CODE_OK
import java.util.*

class FakeUserRepository : IFakeUserRepository {
    private val userMap = mutableMapOf<String, UserDto>()

    init {
        initDefaultUserMap()
    }

    private fun initDefaultUserMap() {
        for (i in 0..9) {
            lateinit var user: UserDto
            when (i) {
                0 -> {
                    user = UserDto(UUID.randomUUID().toString(), "admin", "admin")
                }
                in 1..9 -> {
                    user = UserDto(UUID.randomUUID().toString(), "User_$i", "password$i")
                }
            }
            userMap[user.userLogin] = user
        }
    }

    override fun checkUser(login: String, password: String) =
        if (!userMap.containsKey(login)) {
            REQUEST_CODE_LOGIN_NOT_REGISTERED
        } else if (userMap[login]?.userPassword != password) {
            REQUEST_CODE_INVALID_PASSWORD
        } else {
            REQUEST_CODE_OK
        }

    override fun remindUserPassword(login: String) =
        userMap[login]?.userPassword ?: "Логин \"${login}\" не зарегистрирован"

    override fun registerNewUser(login: String, password: String) =
        if (userMap.containsKey(login)) {
            REQUEST_CODE_LOGIN_REGISTERED_YET
        } else {
            userMap[login] = UserDto(UUID.randomUUID().toString(), login, password)
            REQUEST_CODE_OK
        }
}