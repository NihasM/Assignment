package com.noble.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import com.noble.assignment.SharedPreferencesUtils
import com.noble.assignment.databinding.ActivityMainBinding
import com.noble.assignment.network.ResponseHandler
import com.noble.assignment.room.Users
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ActivityBase() {
    private var viewModel: MainViewModel? = null
    private var binding: ActivityMainBinding? = null
    private var isApiCalled: Boolean = false
    private var loadprogress: Boolean = false
    private var reloadClickListner: ReloadClickListner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel?.initRetrofit(this)
        isApiCalled = SharedPreferencesUtils.getBoolean(this, "isApiCalled", false)
        Log.d("kool", "onCreate:00 "+ SharedPreferencesUtils.getBoolean(this, "isApiCalled", false))

        callApi()

        attachObservables()
        setNavigationHostFragment(supportFragmentManager.findFragmentById(R.id.splash_host) as NavHostFragment)
        setSupportActionBar(binding?.toolbar)
        setupDrawer()
        binding?.toolbar?.setNavigationIcon(R.drawable.ic_menu_new)

        binding?.imgReload?.setOnClickListener {
            binding?.imgReload?.animate()?.rotation(360f)?.start()
            isApiCalled = false
            loadprogress = true
            callApi()
        }
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this, binding?.drawerLayout, binding?.toolbar, R.string.drawer_open, R.string.drawer_close
        )
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        binding?.navigationView?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.userListFragment -> {
                    navHostFragment.navController.navigate(R.id.userListFragment)
                    binding?.drawerLayout?.closeDrawers()
                    true
                }
                R.id.imageCaptureFragment -> {
                    navHostFragment.navController.navigate(R.id.imageCaptureFragment)
                    binding?.drawerLayout?.closeDrawers()
                    true
                }
                R.id.nav_logout -> {
                    navHostFragment.navController.navigate(R.id.pdfViewerFragment)
                    binding?.drawerLayout?.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    fun callApi(){
        viewModel?.getUserData()
    }

    private fun attachObservables() {
        viewModel?.userListResponse?.observe(this) { response ->
            when (response) {
                is ResponseHandler.Loading -> {
                    if(loadprogress){
                        showProgressBar(true)
                    }

                }is ResponseHandler.OnFailed -> {
                showProgressBar(false)
                }
                is ResponseHandler.OnSuccessResponse -> {

                    if(!isApiCalled){
                        lifecycleScope.launch {
                            viewModel?.deleteData()

                            response.response.forEach {

                                viewModel?.insertUsersData(Users(username = it.actName?: "", userId = it.actid?:""))
                            }
                        }
                        SharedPreferencesUtils.putBoolean(this, "isApiCalled", true)
                        reloadClickListner?.onReloadClick()
                    }



                }
                else -> {

                }
            }

        }

    }


    fun setTitle(title:String,reload:Boolean = false,toolbar:Boolean = true){
        binding?.tvTitle?.text = title
        if(reload){
            binding?.imgReload?.visibility = View.VISIBLE
        }else{
            binding?.imgReload?.visibility = View.GONE
        }

        if(toolbar){
            binding?.toolbar?.visibility = View.VISIBLE
            binding?.tvTitle?.visibility = View.VISIBLE
        }else{
            binding?.toolbar?.visibility = View.GONE
            binding?.tvTitle?.visibility = View.GONE
        }
    }

    fun showProgressBar(progressLoader:Boolean){
        if(progressLoader){
            binding?.progressLoader?.root?.visibility = View.VISIBLE
        }else{
            binding?.progressLoader?.root?.visibility = View.GONE
        }
    }

    fun setPopupCallback(callback: ReloadClickListner) {
        reloadClickListner = callback
    }



}