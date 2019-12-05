package com.example.vlad.deliveryinstantapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
//import com.anychart.anychart.AnyChart
//import com.anychart.anychart.AnyChartView
//import com.anychart.anychart.DataEntry
//import com.anychart.anychart.ValueDataEntry
import com.example.vlad.deliveryinstantapp.model.Order
import com.example.vlad.deliveryinstantapp.model.Stuff
import com.google.firebase.firestore.FirebaseFirestore

class Charts2Activity : AppCompatActivity() {
    private val TAG = "ChartsActivity2"

    private var firestoreDB: FirebaseFirestore? = null
    val stuffs = mutableListOf<Stuff>()
    val stuffscount = mutableListOf<A2>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        loadNotesListFor2Chart()
        populaltesListFor2Chart()
    }

    private fun loadNotesListFor2Chart() {
        firestoreDB = FirebaseFirestore.getInstance()

        var number = 0
        firestoreDB!!.collection("orders").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                for (document in task.result!!) {

                    for(i in stuffscount){
                        val order = document.toObject<Order>(Order::class.java)
                        if(order.titleo == i.title)
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

                    val pie = AnyChart.pie()
                    //val bar = AnyChart.bar()

                    val data = ArrayList<DataEntry>()

                    for(i in stuffscount){
                        data.add(ValueDataEntry(i.title, i.count))
                    }
//                    data.add(ValueDataEntry("weqre",5))
//                    data.add(ValueDataEntry("jgkhcx",3))
//                    data.add(ValueDataEntry("fjuyd",8))
                    pie.data(data)

                    val anyChartView = findViewById<View>(R.id.anychartview) as AnyChartView
                    anyChartView.setChart(pie)





                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
    private fun populaltesListFor2Chart() {

        firestoreDB!!.collection("notes")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val notesList = mutableListOf<Stuff>()

                    for (doc in task.result!!) {
                        val note = doc.toObject<Stuff>(Stuff::class.java)
                        note.id = doc.id
                        notesList.add(note)


                    }
                    for(i in notesList){
                        stuffscount.add(A2(i.title.toString(), 0))
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
