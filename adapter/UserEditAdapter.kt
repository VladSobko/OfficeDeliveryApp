package com.example.vlad.deliveryinstantapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Switch
import android.widget.TextView

import com.example.vlad.deliveryinstantapp.R
import com.example.vlad.deliveryinstantapp.model.User

class UserEditAdapter(var items: ArrayList<User>) : RecyclerView.Adapter<UserEditAdapter.ViewHolder>() {

    var onBanClicked: ((User, Boolean) -> Unit)? = null


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemView = LayoutInflater.from(p0.context)
            .inflate(R.layout.list_item_user, p0, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        var userDto = items[p1]

        p0.tvUsername?.text = userDto.username
        p0.tvEmail?.text = userDto.email
        p0.ban?.isChecked = userDto.ban!!


        p0.ban?.setOnCheckedChangeListener { _, isChecked ->
            onBanClicked?.invoke(userDto, isChecked)

        }

    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var tvUsername: TextView? = null
        var tvEmail: TextView? = null
        var ban: Switch? = null

        init {
            tvUsername = row.findViewById(R.id.tvUsername)
            tvEmail = row.findViewById(R.id.tvEmail)
            ban = row.findViewById(R.id.switchBan)
        }
    }

    fun setFilter(newList: List<User>) {
        items = arrayListOf()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}