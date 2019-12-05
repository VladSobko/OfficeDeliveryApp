package com.example.vlad.deliveryinstantapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.util.Log

import java.util.ArrayList

import android.view.View


import com.example.vlad.deliveryinstantapp.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.vlad.deliveryinstantapp.adapter.UserEditAdapter
import com.example.vlad.deliveryinstantapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FirstFragment : Fragment() {

    private val TAG = "ChartsActivity1"

    private var firestoreDB: FirebaseFirestore? = null


    val users = ArrayList<User>()
    val adapter = UserEditAdapter(users)

    val userscount = ArrayList<A1>()

    override fun onResume() {
        super.onResume()
        loadNotesList()
        populaltesList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loadNotesList()
        populaltesList()
        return inflater.inflate(R.layout.fragment_first, container, false)
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
//                    val bar = AnyChart.bar()
//
//                    val data = ArrayList<DataEntry>()
//
//                    for(i in userscount){
//                        data.add(ValueDataEntry(i.email, i.count))
//                    }
//
//                    bar.data(data)
//
//                    val anyChartView = view?.findViewById<View>(R.id.any_chart_view) as AnyChartView
//                    anyChartView.setChart(bar)




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

}
