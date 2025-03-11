package com.noble.assignment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

abstract class ActivityBase : AppCompatActivity() {

    lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    /**
     * This method is used for Navigating from One Screen to Next Screen using Navigation
     * Direction graph.
     * @param navigationId This is the Id of the Navigation Graph Action
     */
    fun navigateToNextScreen(navigationId: Int) {
        try {
            navHostFragment.findNavController().navigate(navigationId)
        } catch (e: Exception) {

        }
    }


    /**
     * This method is used for Navigating from One Screen to Next Screen using Navigation
     * Direction graph.
     * @param action This is the Id of the Navigation Graph Action
     */
    fun navigateToNextScreen(action: NavDirections) {
        try {
            navHostFragment.findNavController().navigate(action)
        } catch (e: Exception) {

        }
    }

    /**
     * This is the Method to initialize the variable at base level for Navigating from Single Class.
     * @param navHostFragment This is the Id of the NavHost Fragment or  FragmentContainerView.
     */
    fun setNavigationHostFragment(navHostFragment: NavHostFragment) {
        this.navHostFragment = navHostFragment
    }


    fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.splash_host)
    }


    /**
     * This is the method used for setup the Configuration change with Language locale.
     *
     */
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

}