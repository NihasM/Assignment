package com.noble.assignment.HomeScreen.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.noble.assignment.HomeScreen.itemClickListener
import com.noble.assignment.databinding.ActivityMainBinding
import com.noble.assignment.databinding.UserListItemAdapterBinding
import com.noble.assignment.room.Users

class UserListingAdapter:RecyclerView.Adapter<UserListingAdapter.UserListingViewHolder>() {
    var userList = mutableListOf<Users>()
    private var listener : itemClickListener? = null

    inner class UserListingViewHolder(val binding: UserListItemAdapterBinding) :RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener?.onUserClick(userList[absoluteAdapterPosition].username,userList[absoluteAdapterPosition].alternateName,userList[absoluteAdapterPosition].userId,userList[absoluteAdapterPosition].id)
            }
        }

        fun bind(position: Int) {
            val currentItem = userList[position]
            binding.tvName.text = currentItem.username
            if(currentItem.alternateName.isNotEmpty()){
                binding.tvAlternateName.text = currentItem.alternateName
            }else{
                binding.tvAlternateName.text = "-"
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): UserListingAdapter.UserListingViewHolder {
        return UserListingViewHolder(
            UserListItemAdapterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserListingAdapter.UserListingViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = userList.size

    fun setUserData(list:List<Users>){
        notifyItemRangeRemoved(0, userList.size)
        userList.clear()
        userList.addAll(list)
        notifyItemRangeInserted(0, userList.size)
    }
    fun setListener(itemListener: itemClickListener) {
        listener = itemListener
    }

    fun removeItem(position: Int) {
        listener?.deleteUser(userList[position].id,userList[position].userId)
        userList.removeAt(position)
        notifyItemRemoved(position)
    }

}