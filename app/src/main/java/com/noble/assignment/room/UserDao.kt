package com.noble.assignment.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Users)

    @Query("SELECT * FROM users_table")
    suspend fun getAllUsers(): List<Users>

    @Query("DELETE FROM users_table")
    suspend fun deleteAllItems()

    @Update
    suspend fun updateUser(user: Users)

    @Query("DELETE FROM users_table WHERE id = :id")
    suspend fun deleteUserById(id: Long)

}