package com.noble.assignment.HomeScreen

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noble.assignment.HomeScreen.adapter.UserListingAdapter
import com.noble.assignment.MainActivity
import com.noble.assignment.MainViewModel
import com.noble.assignment.R
import com.noble.assignment.ReloadClickListner
import com.noble.assignment.databinding.DialogEditUserDataBinding
import com.noble.assignment.databinding.FragmentUserListBinding
import com.noble.assignment.room.MyApp
import com.noble.assignment.room.Users
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class UserListFragment : Fragment(),itemClickListener, ReloadClickListner {
    private var viewModel: MainViewModel? = null
    private var binding: FragmentUserListBinding? = null
    private var userListingAdapter: UserListingAdapter? = null
    private var dialogEditUserDataBinding : DialogEditUserDataBinding? = null
    private var editDialog: Dialog? = null
    private val REQUEST_CODE_SPEECH_INPUT = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_list, container, false
        )
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        (activity as MainActivity).setTitle("Users",true)
        lifecycleScope.launch {
            (activity as MainActivity).showProgressBar(true)
            delay(500)
            viewModel?.getUserDBData()
        }
        userListingAdapter = UserListingAdapter()
        userListingAdapter?.setListener(this)
        binding?.userListRcy?.adapter = userListingAdapter
        binding?.userListRcy?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        observeUserList()

        val itemTouchHelper = ItemTouchHelper(createSwipeCallback())
        itemTouchHelper.attachToRecyclerView(binding?.userListRcy)

        (activity as MainActivity).setPopupCallback(this)
        return binding?.root
    }



    private fun observeUserList() {
        viewModel?.userList?.observe(viewLifecycleOwner, Observer { users ->
            userListingAdapter?.setUserData(users)
            (activity as MainActivity).showProgressBar(false)
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
    override fun onUserClick(name: String, altername: String, userid: String, id: Long) {
        openEditUserDialog(name,altername,id,userid)
    }

    override fun deleteUser(id: Long, userid: String) {
        lifecycleScope.launch {
            viewModel?.deleteUser(userId = id)
        }

    }
    private fun openEditUserDialog(name: String, altername: String, id: Long,userid: String) {
        editDialog = activity?.let { Dialog(it) }
        dialogEditUserDataBinding = DialogEditUserDataBinding.inflate(layoutInflater, null, false)
        dialogEditUserDataBinding?.root?.let { editDialog?.setContentView(it) }
        editDialog?.getWindow()
            ?.setBackgroundDrawableResource(android.R.color.transparent);
        if (editDialog != null) {
            if (editDialog?.isShowing!!) {
                editDialog?.dismiss()
            }
        }

        editDialog?.show()
        editDialog?.setCanceledOnTouchOutside(false)

        editDialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        dialogEditUserDataBinding?.edtUserName?.setText(name)
        dialogEditUserDataBinding?.edtAlternateName?.setText(altername)

        dialogEditUserDataBinding?.cancelBtn?.setOnClickListener {
            editDialog?.dismiss()
        }

        dialogEditUserDataBinding?.imgMic?.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // on below line we are displaying error message in toast
                Toast
                    .makeText(
                        requireContext(), " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }

        dialogEditUserDataBinding?.btnSave?.setOnClickListener {
            lifecycleScope.launch {
                viewModel?.updateUser(Users(id=id, username = dialogEditUserDataBinding?.edtUserName?.text.toString(),alternateName =dialogEditUserDataBinding?.edtAlternateName?.text.toString(), userId = userid))
                viewModel?.getUserDBData()
                editDialog?.dismiss()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {

            if (resultCode == RESULT_OK && data != null) {
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                dialogEditUserDataBinding?.edtAlternateName?.setText(
                    Objects.requireNonNull(res)[0]
                )
            }
        }
    }

    override fun onReloadClick() {
        Log.d("kool", "onReloadClick: aya")
        lifecycleScope.launch {
            delay(500)
            viewModel?.getUserDBData()
        }
    }

}