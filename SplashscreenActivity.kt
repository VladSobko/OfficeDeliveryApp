package com.example.vlad.deliveryinstantapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        val background = object: Thread(){
            override fun run() {
                try{
                        Thread.sleep(4000)
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
