package com.example.vlad.deliveryinstantapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.vlad.deliveryinstantapp.adapter.ReviewAdapter
import com.example.vlad.deliveryinstantapp.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {

    private val TAG = "ReviewActivity"

    private var mAdapter: ReviewAdapter? = null

    private var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        firestoreDB = FirebaseFirestore.getInstance()

        loadNotesList()

        firestoreListener = firestoreDB!!.collection("reviews")
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e)
                    return@EventListener
                }

                val notesList = mutableListOf<Review>()

                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val note = doc.toObject(Review::class.java)
                        note.id = doc.id
                        notesList.add(note)

                    }
                }
                mAdapter = ReviewAdapter(notesList, applicationContext, firestoreDB!!)
                rvwNoteList.adapter = mAdapter
            })

    }
    private fun loadNotesList() {
        firestoreDB!!.collection("reviews")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val notesList = mutableListOf<Review>()



                    for (doc in task.result!!) {
                        val note = doc.toObject<Review>(Review::class.java)
                        note.id = doc.id

                        notesList.add(note)

                    }

                    mAdapter = ReviewAdapter(notesList, applicationContext, firestoreDB!!)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
                    rvwNoteList.layoutManager = mLayoutManager
                    rvwNoteList.itemAnimator = DefaultItemAnimator()
                    rvwNoteList.adapter = mAdapter
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
