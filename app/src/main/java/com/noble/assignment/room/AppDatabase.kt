package com.noble.assignment.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Users::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}