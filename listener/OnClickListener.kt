package com.example.vlad.deliveryinstantapp.listener

import com.example.vlad.deliveryinstantapp.model.Stuff

interface OnClickListener {

    fun onItemClick(u: Stuff)

    fun onItemDelete(u: Stuff)


}