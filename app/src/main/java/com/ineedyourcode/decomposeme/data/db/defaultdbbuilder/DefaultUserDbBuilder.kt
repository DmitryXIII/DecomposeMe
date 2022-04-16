package com.ineedyourcode.decomposeme.data.db.defaultdbbuilder

import com.ineedyourcode.decomposeme.data.db.UserDao
import com.ineedyourcode.decomposeme.data.db.UserEntity
import com.ineedyourcode.decomposeme.data.utils.MockDatabaseConstants
import java.util.*

class DefaultUserDbBuilder(private val roomDataSource: UserDao) {
    fun initDefaultUserDataBase() {
        for (i in 0..9) {
            when (i) {
                0 -> {
                    roomDataSource.createUser(
                        UserEntity(
                            UUID.randomUUID().toString(),
                            MockDatabaseConstants.DefaultUsers.DEFAULT_ADMIN_LOGIN.value,
                            MockDatabaseConstants.DefaultUsers.DEFAULT_ADMIN_PASSWORD.value
                        )
                    )
                }

                in 1..9 -> {
                    roomDataSource.createUser(
                        UserEntity(
                            UUID.randomUUID().toString(),
                            MockDatabaseConstants.DefaultUsers.DEFAULT_USER_LOGIN.value + i,
                            MockDatabaseConstants.DefaultUsers.DEFAULT_USER_PASSWORD.value + i
                        )
                    )
                }
            }
        }
    }
}