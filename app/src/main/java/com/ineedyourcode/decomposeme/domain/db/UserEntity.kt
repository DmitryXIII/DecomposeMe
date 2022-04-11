package com.ineedyourcode.decomposeme.domain.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val userLogin: String,
    val userPassword: String,
    val isAuthorized: Boolean
)