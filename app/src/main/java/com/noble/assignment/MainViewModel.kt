package com.noble.assignment

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.noble.assignment.network.ApiInterface
import com.noble.assignment.network.ResponseHandler
import com.noble.assignment.network.RetrofitInstance
import com.noble.assignment.room.MyApp
import com.noble.assignment.room.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private var apiInterface: ApiInterface? = null
    val userListResponse = MutableLiveData<ResponseHandler<Array<UserListResponse.Data>>>()
    var userList = MutableLiveData<List<Users>>()
    val dbCount = MutableLiveData<Int>()

    fun initRetrofit(context: Context) {
        RetrofitInstance.initRetrofit(context)
        apiInterface = RetrofitInstance.getApiInterface()
    }

    suspend fun insertUsersData(users: Users) {
            MyApp.database.userDao().insertUser(users)
    }

    suspend fun getUserDBData(){
        delay(1000)
        userList.postValue(emptyList())
        val usersList = MyApp.database.userDao().getAllUsers()
        userList.postValue(usersList)
    }

    suspend fun deleteData(){
        MyApp.database.userDao().deleteAllItems()
    }

    suspend fun updateUser(users: Users){
        MyApp.database.userDao().updateUser(users)
    }

    suspend fun deleteUser(userId:Long){
        MyApp.database.userDao().deleteUserById(userId)
    }
    fun getUserData(){
        viewModelScope.launch {
            userListResponse.value = ResponseHandler.Loading
            try {
                val response = apiInterface?.userList()
                if (response != null && response.isSuccessful) {

                    val responseBody = response.body()?.toString()
                    val data  = Gson().fromJson(responseBody, Array<UserListResponse.Data>::class.java)
                    userListResponse.value = ResponseHandler.OnSuccessResponse(data)

                } else {
                    userListResponse.value =
                        ResponseHandler.OnFailed(response?.code() ?: -1, response?.message(), "")
                    Log.d("kool", "getUserData: F"+ response?.code()+"/"+ response?.message(), )
                }
            }catch (e: Exception) {
                e.printStackTrace()
                userListResponse.value = ResponseHandler.OnFailed(-1, e.message, "")
            }
        }
    }

    fun isUserListEmpty(): Boolean {
        return userList.value.isNullOrEmpty()
    }
}