package com.example.vlad.deliveryinstantapp


import android.os.Bundle
import android.support.v7.app.AppCompatActivity


import android.util.Log
import android.view.Menu
import android.view.MenuItem

import java.util.ArrayList


import android.view.View
import android.widget.Toast
//import com.anychart.anychart.AnyChart
//import com.anychart.anychart.AnyChartView
//import com.anychart.anychart.DataEntry
//import com.anychart.anychart.ValueDataEntry
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry

import com.example.vlad.deliveryinstantapp.adapter.UserEditAdapter

import com.example.vlad.deliveryinstantapp.model.Order
import com.example.vlad.deliveryinstantapp.model.Stuff
import com.example.vlad.deliveryinstantapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException


class ChartsActivity : AppCompatActivity() {
    private val TAG = "ChartsActivity"

    private var firestoreDB: FirebaseFirestore? = null
    val users = ArrayList<User>()
    val adapter = UserEditAdapter(users)
    val userscount = ArrayList<A1>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)
        firestoreDB = FirebaseFirestore.getInstance()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadChart1()



//        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
//        viewpager.adapter = fragmentAdapter
//
//        tabs.setupWithViewPager(viewpager)

    }

    private fun loadChart1(){
        loadNotesList()
        populaltesList()
    }


    private fun loadNotesList() {
        firestoreDB = FirebaseFirestore.getInstance()

        var number = 0
        firestoreDB!!.collection("orders").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                for (document in task.result!!) {

                    for(i in userscount){
                        val order = document.toObject<Order>(Order::class.java)
                        if(order.user == i.email)
                            i.count = i.count?.plus(1)

                        //data.add(ValueDataEntry(i.user, number))
                    }

                    number++
                }
                Log.d("TAG", number.toString() + "")
            } else {

            }
        }






        firestoreDB!!.collection("orders")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val notesList = mutableListOf<Order>()

                    for (doc in task.result!!) {
                        val note = doc.toObject<Order>(Order::class.java)
                        note.id = doc.id
                        notesList.add(note)

                    }

                    //        val pie = AnyChart.pie()
                    try {
                        val bar = AnyChart.bar()

                        val data = ArrayList<DataEntry>()

                        for (i in userscount) {
                            data.add(ValueDataEntry(i.email, i.count))
                        }

                        bar.data(data)

                        val anyChartView = findViewById<View>(R.id.any_chart_view) as AnyChartView
                        anyChartView.setChart(bar)
                    }
                    catch ( e: IOException) {

                    }





                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
    private fun populaltesList() {

        val mDatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val mListenerPopulateList = mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for (convSnapshot in dataSnapshot.children) {
                    val conv = convSnapshot.getValue(User::class.java)
                    if (conv?.role == false)
                        users.add(conv)

                }


                for(i in users){
                    userscount.add(A1(i.email.toString(), 0))
                }
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
