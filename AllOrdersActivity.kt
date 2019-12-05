package com.example.vlad.deliveryinstantapp

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import com.example.vlad.deliveryinstantapp.adapter.AllOrderAdapter
import com.example.vlad.deliveryinstantapp.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_all_orders.*

class AllOrdersActivity : BaseActivity() {

    private val TAG = "AllOrdersActivity"


    private var mAdapter: AllOrderAdapter? = null

    private var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_orders)

        firestoreDB = FirebaseFirestore.getInstance()

        loadNotesList()

        button_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@AllOrdersActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT).show()
        }


        val drawable = GradientDrawable()
        drawable.setColor(getColor(R.color.colorPrimaryDark))
        drawable.cornerRadius = resources.getDimension(R.dimen.radius) // пример 16dp

        button_logout.background = drawable

//        firestoreListener = firestoreDB!!.collection("orders")
//            .addSnapshotListener(EventListener { documentSnapshots, e ->
//                if (e != null) {
//                    Log.e(TAG, "Listen failed!", e)
//                    return@EventListener
//                }
//
//                val notesList = mutableListOf<Order>()
//
//                if (documentSnapshots != null) {
//                    for (doc in documentSnapshots) {
//                        val note = doc.toObject(Order::class.java)
//                        note.id = doc.id
//                        notesList.add(note)
//                        runAnimation(rvNoteListAO!!)
//                    }
//                }
//
//                mAdapter = AllOrderAdapter(notesList, applicationContext, firestoreDB!!)
//                rvNoteListAO.adapter = mAdapter
//            })
    }
    private fun runAnimation(mRecyclerView: RecyclerView) {
        val context : Context = mRecyclerView.context
        val controller: LayoutAnimationController?
        controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
        mRecyclerView.adapter = mAdapter

        mRecyclerView.layoutAnimation = controller
        mAdapter?.notifyDataSetChanged()
        mRecyclerView.scheduleLayoutAnimation()

    }
    override fun onDestroy() {
        super.onDestroy()

        firestoreListener!!.remove()
    }

    private fun loadNotesList() {
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

                    mAdapter = AllOrderAdapter(notesList, applicationContext, firestoreDB!!)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
                    rvNoteListAO.layoutManager = mLayoutManager
                    rvNoteListAO.itemAnimator = DefaultItemAnimator()
                    rvNoteListAO.adapter = mAdapter
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val item : MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                searchData(query)
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun searchData(query: String) {
        showProgressSearch()
        firestoreDB!!.collection("orders").whereEqualTo("client", query.toLowerCase())
            .get()
            .addOnCompleteListener { task ->
                val notesList = mutableListOf<Order>()
                notesList.clear()
                hideProgressDialog()

                for (doc in task.result!!) {
                    val note = doc.toObject<Order>(Order::class.java)
                    note.id = doc.id
                    notesList.add(note)
                }

                mAdapter = AllOrderAdapter(notesList, applicationContext, firestoreDB!!)
                val mLayoutManager = LinearLayoutManager(applicationContext)
                rvNoteListAO.layoutManager = mLayoutManager
                rvNoteListAO.itemAnimator = DefaultItemAnimator()
                rvNoteListAO.adapter = mAdapter


            }
            .addOnFailureListener{ e ->
                hideProgressDialog()
                Toast.makeText(this, "Nothing not found", Toast.LENGTH_SHORT).show()

            }

    }
    private fun sortListByPrice(){
        firestoreListener = firestoreDB!!.collection("orders").orderBy("priceo", Query.Direction.ASCENDING)
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e)
                    return@EventListener
                }

                val notesList = mutableListOf<Order>()
                val user = FirebaseAuth.getInstance().currentUser
                val userEmail = user!!.email


                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val note = doc.toObject(Order::class.java)
                        note.id = doc.id
                        if(note.user == userEmail){
                            notesList.add(note)
                        }
                    }
                }

                mAdapter = AllOrderAdapter(notesList, applicationContext, firestoreDB!!)
                rvNoteListAO.adapter = mAdapter
            })
    }
    private fun sortListByTitle(){
        firestoreListener = firestoreDB!!.collection("orders").orderBy("titleo", Query.Direction.ASCENDING)
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e)
                    return@EventListener
                }

                val notesList = mutableListOf<Order>()
                val user = FirebaseAuth.getInstance().currentUser
                val userEmail = user!!.email


                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val note = doc.toObject(Order::class.java)
                        note.id = doc.id
                        if(note.user == userEmail){
                            notesList.add(note)
                        }
                    }
                }

                mAdapter = AllOrderAdapter(notesList, applicationContext, firestoreDB!!)
                rvNoteListAO.adapter = mAdapter
            })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.sortTitle) {
            sortListByTitle()
            Toast.makeText(this, "Посортовано по назві", Toast.LENGTH_SHORT).show()
        }
        if (item!!.itemId == R.id.sortPrice) {
            sortListByPrice()
            Toast.makeText(this, "Посортовано по ціні", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}
