package com.ineedyourcode.decomposeme.domain.fakerepository

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
            401
        } else if (userMap[login]?.userPassword != password) {
            403
        } else {
            200
        }

    override fun remindUserPassword(login: String) =
        userMap[login]?.userPassword ?: "Логин \"${login}\" не зарегистрирован"
}