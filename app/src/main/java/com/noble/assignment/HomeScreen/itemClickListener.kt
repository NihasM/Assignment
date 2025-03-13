package com.noble.assignment.HomeScreen

interface itemClickListener {

    fun onUserClick(name: String, altername: String,userid:String, id: Long) {}

    fun deleteUser(id: Long,userid: String) {}
}