package com.noble.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.noble.assignment.databinding.ActivityMainBinding
import com.noble.assignment.network.ResponseHandler

class MainActivity : AppCompatActivity() {
    private var viewModel: MainViewModel? = null
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel?.initRetrofit(this)
        viewModel?.getUserData()
        attachObservables()
        binding?.toolbar?.setNavigationIcon(R.drawable.ic_menu_new)
    }   


    private fun attachObservables() {
        viewModel?.userListResponse?.observe(this) { response ->
            when (response) {
                is ResponseHandler.Loading -> {

                }is ResponseHandler.OnFailed -> {

                }
                is ResponseHandler.OnSuccessResponse -> {
                   response.response.forEach {
                       Log.d("kool", "actName: "+it.actName)
                   }

                }
                else -> {

                }
            }

        }
    }
}