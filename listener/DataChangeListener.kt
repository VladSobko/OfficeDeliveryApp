package com.example.vlad.deliveryinstantapp.listener

import com.example.vlad.deliveryinstantapp.model.Stuff

interface DataChangeListener {
    fun onDataLoaded(stuffs: MutableList<Stuff>)
}