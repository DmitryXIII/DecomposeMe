package com.ineedyourcode.decomposeme.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val userLogin: String,
    val userPassword: String,
    val isAuthorized: Boolean = false
)