package com.noble.assignment.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class Users(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userId: String,
    val alternateName: String = ""
    )
