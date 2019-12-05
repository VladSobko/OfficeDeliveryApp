package com.example.vlad.deliveryinstantapp.database

import android.util.Log
import android.widget.Toast
import com.example.vlad.deliveryinstantapp.adapter.MyAdapter
import com.example.vlad.deliveryinstantapp.listener.DataChangeListener
import com.example.vlad.deliveryinstantapp.model.Stuff
import com.google.firebase.database.*

class StuffDaoImpl : StuffDao {
    override fun fetchAllStuffs(listener: DataChangeListener): MutableList<Stuff> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addStuff() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /*val firebaseDatabase = FirebaseDatabase.getInstance()

    override fun addStuff() {
        val appDatabaseReference = firebaseDatabase.reference.child("Stuffs")
        val stuffId = appDatabaseReference.push().key
        val stuff = Stuff("uuu", "qwerty","12".toDouble(), "22".toInt(),"vghvgvh", "ghjghjgj", 4)
        appDatabaseReference.child(stuffId!!).setValue(stuff).addOnCompleteListener {
            Log.v("addddding", "gooood")
        }
    }

    private var stuffs: MutableList<Stuff>? = null
    private var mListener: DataChangeListener? = null
    override fun fetchAllStuffs(listener: DataChangeListener): MutableList<Stuff> {
        mListener = listener

        val appDatabaseReference = firebaseDatabase.reference.child("Stuffs")
        val fetchAppInfoQuery = appDatabaseReference.orderByKey()
        fetchAppInfoQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(s in p0.children){
                        val stuff = s.getValue(Stuff::class.java)
                        stuffs!!.add(stuff!!)
                    }
                    stuffs?.let { mListener!!.onDataLoaded(it) }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, databaseError.message)
            }
        })
        return stuffs!!
    }

    companion object {
        private val TAG = StuffDaoImpl::class.java.name
    }*/
}
