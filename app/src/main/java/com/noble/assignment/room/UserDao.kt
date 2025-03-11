package com.noble.assignment.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Users)

    @Query("SELECT * FROM users_table")
    suspend fun getAllUsers(): List<Users>

    @Query("SELECT COUNT(*) FROM your_table_name")
    fun getRowCount(): Int
}