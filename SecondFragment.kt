package com.example.vlad.deliveryinstantapp


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast


class SecondFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        print("qwertyuiop")
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

}