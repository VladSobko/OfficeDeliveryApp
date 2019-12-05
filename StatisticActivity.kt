package com.example.vlad.deliveryinstantapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.vlad.deliveryinstantapp.model.Order
import com.example.vlad.deliveryinstantapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_statistic.*
import java.util.ArrayList

import com.google.firebase.firestore.QuerySnapshot
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class StatisticActivity : AppCompatActivity() {
    private val TAG = "StatisticActivity"

    private var firestoreDB: FirebaseFirestore? = null
    val users = ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        Picasso.with(imageViewProst.context)
            .load("http://avenuegifts.com.ua/media/mod_ninja_simple_icon_menu/images/galantereya.png")
            .placeholder(android.R.drawable.alert_light_frame)
            .error(android.R.drawable.alert_dark_frame)
            .into(imageViewProst)

        firestoreDB = FirebaseFirestore.getInstance()

        firestoreDB!!.collection("orders")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var numberZ = 0
                    for (document in task.result!!) {
                        numberZ++

                    }
                    tvZ.text = numberZ.toString()
                    Log.d(TAG, numberZ.toString())
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }



        firestoreDB!!.collection("orders")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful()) {
                    var numberD = 0
                    for (document in task.result!!) {
                        val order = document.toObject<Order>(Order::class.java)
                        if(order.done) numberD++

                    }
                    tvD.text = numberD.toString()
                    Log.d(TAG, numberD.toString())
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException())
                }
            }



        val mDatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val mListenerPopulateList = mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var numberK = 0
                for (convSnapshot in dataSnapshot.children) {
                    val conv = convSnapshot.getValue(User::class.java)
                    if (conv?.role == false){
                        users.add(conv)
                        numberK++
                    }

                }
                tvK.text = numberK.toString()
                Log.d("TAG", numberK.toString() + "")
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        mDatabaseReference.addListenerForSingleValueEvent(mListenerPopulateList)

    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
