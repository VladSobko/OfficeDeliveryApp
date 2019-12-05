package com.example.vlad.deliveryinstantapp.database

import com.example.vlad.deliveryinstantapp.listener.DataChangeListener

import com.example.vlad.deliveryinstantapp.model.Stuff

interface StuffDao {
    fun fetchAllStuffs(listener: DataChangeListener): MutableList<Stuff>
    fun addStuff()
}