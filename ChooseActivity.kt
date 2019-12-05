package com.example.vlad.deliveryinstantapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_choose.*

class ChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        Picasso.with(imageView.context)
            .load("https://static7.depositphotos.com/1076754/699/v/950/depositphotos_6995635-stock-illustration-business-and-office-supplies-vector.jpg")
            .placeholder(android.R.drawable.alert_dark_frame)
            .error(android.R.drawable.alert_dark_frame)
            .into(imageView)

        Picasso.with(imageView2.context)
            .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjoo4-24LzTmOdplhoqMzMzJDV1dOIqfAMiHScAsE4Luo3Ga-dMg")
            .placeholder(android.R.drawable.alert_dark_frame)
            .error(android.R.drawable.alert_dark_frame)
            .into(imageView2)

        button_stuff.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
        button_users.setOnClickListener {
            startActivity(Intent(this, AdminUserActivity::class.java))
        }
    }
}
