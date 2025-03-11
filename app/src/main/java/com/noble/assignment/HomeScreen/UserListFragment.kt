package com.noble.assignment.HomeScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.noble.assignment.MainViewModel
import com.noble.assignment.R
import com.noble.assignment.databinding.FragmentUserListBinding
import com.noble.assignment.room.MyApp
import com.noble.assignment.room.Users
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class UserListFragment : Fragment() {
    private var viewModel: MainViewModel? = null
    private var binding: FragmentUserListBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_list, container, false
        )
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        lifecycleScope.launch {
            viewModel?.getUserDBData()
        }

        observeUserList()
        return binding?.root
    }

    private fun observeUserList() {
        viewModel?.userList?.observe(viewLifecycleOwner, Observer { users ->
            Log.d("kool", "observeUserList: "+users)
        })
    }




}