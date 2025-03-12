package com.noble.assignment.HomeScreen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noble.assignment.HomeScreen.adapter.UserListingAdapter
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
    private var userListingAdapter: UserListingAdapter? = null
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
            delay(500)
            viewModel?.getUserDBData()
        }
        userListingAdapter = UserListingAdapter()
        binding?.userListRcy?.adapter = userListingAdapter
        binding?.userListRcy?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        observeUserList()

        val itemTouchHelper = ItemTouchHelper(createSwipeCallback())
        itemTouchHelper.attachToRecyclerView(binding?.userListRcy)
        return binding?.root
    }

    private fun observeUserList() {
        viewModel?.userList?.observe(viewLifecycleOwner, Observer { users ->
            userListingAdapter?.setUserData(users)
        })
    }


    private fun createSwipeCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // This method is used to draw the background and the delete icon
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                val itemView = viewHolder.itemView

                // Set the background color for the swipe (red background)
                if (dX < 0) {  // Swipe to the left
                    val background = Color.RED
                    c.drawColor(background)  // Red background color

                    // Get the delete icon and draw it
                    val deleteIcon: Drawable? = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_delete  // Replace with your delete icon
                    )

                    deleteIcon?.let {
                        val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                        val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                        val iconTop = itemView.top + iconMargin
                        val iconRight = itemView.right - iconMargin
                        val iconBottom = itemView.bottom - iconMargin
                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        it.draw(c)  // Draw the delete icon
                    }
                }
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // Perform the delete operation
                userListingAdapter?.removeItem(position)
            }
        }
    }

}