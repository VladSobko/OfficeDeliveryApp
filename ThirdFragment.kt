package com.example.vlad.deliveryinstantapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_third.*


class ThirdFragment : Fragment() {


    //private var firestoreDB: FirebaseFirestore? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
//        Picasso.with(imageViewProst.context)
//            .load("http://avenuegifts.com.ua/media/mod_ninja_simple_icon_menu/images/galantereya.png")
//            .placeholder(android.R.drawable.alert_light_frame)
//            .error(android.R.drawable.alert_dark_frame)
//            .into(imageViewProst)
//
//        firestoreDB = FirebaseFirestore.getInstance()
//
//        var numberZ = 0
//        var numberD = 0
//        var numberA = 0
//        firestoreDB!!.collection("orders").get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//
//                for (document in task.result!!) {
//                    numberZ++
//                }
//                Log.d("TAG", numberZ.toString() + "")
//            } else {
//
//            }
//        }
//        tvZ.text = numberZ.toString()



        return inflater.inflate(R.layout.fragment_third, container, false)
    }

}