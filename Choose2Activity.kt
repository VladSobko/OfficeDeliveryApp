package com.example.vlad.deliveryinstantapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_choose2.*

class Choose2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose2)

        Picasso.with(imageView.context)
            .load("https://www.budgetnik.ru/images/articles/103146/5.jpg")
            .placeholder(android.R.drawable.alert_dark_frame)
            .error(android.R.drawable.alert_dark_frame)
            .into(imageView)

        Picasso.with(imageView2.context)
            .load("http://jewelerclub.info/wp-content/uploads/2016/10/img.jpg")
            .placeholder(android.R.drawable.alert_dark_frame)
            .error(android.R.drawable.alert_dark_frame)
            .into(imageView2)

        button_Chart1.setOnClickListener {
            startActivity(Intent(this, ChartsActivity::class.java))
        }
        button_Chart2.setOnClickListener {
            startActivity(Intent(this, Charts2Activity::class.java))
        }
        button_All.setOnClickListener {
            startActivity(Intent(this, StatisticActivity::class.java))
        }

    }
}
