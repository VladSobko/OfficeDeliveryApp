package com.example.vlad.deliveryinstantapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import android.widget.Toast

import com.example.vlad.deliveryinstantapp.adapter.MyAdapter
import com.example.vlad.deliveryinstantapp.model.Stuff
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_admin.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_admin.*
import java.util.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import android.support.v4.view.ViewCompat.setNestedScrollingEnabled
import android.support.v7.widget.RecyclerView
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import com.facebook.internal.Mutable
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import java.lang.Exception

class AdminActivity :  BaseActivity() {

    private val TAG = "AdminActivity"

    private var mAdapter: MyAdapter? = null

    private var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null
    val notesList = mutableListOf<Stuff>()

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        firestoreDB = FirebaseFirestore.getInstance()

        loadNotesList()
        fab.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        logout_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@AdminActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT).show()
        }
        allOrders_btn.setOnClickListener {

            val intent = Intent(this@AdminActivity, AllOrdersActivity::class.java)
            startActivity(intent)

        }
        btnStatistic.setOnClickListener {

            val intent = Intent(this@AdminActivity, Choose2Activity::class.java)
            startActivity(intent)

        }
        val drawable = GradientDrawable()
        drawable.setColor(getColor(R.color.colorPrimaryDark))
        drawable.cornerRadius = resources.getDimension(R.dimen.radius) // пример 16dp

        logout_btn.background = drawable
        allOrders_btn.background = drawable
        btnStatistic.background = drawable


        firestoreListener = firestoreDB!!.collection("notes")
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e)
                    return@EventListener
                }



                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val note = doc.toObject(Stuff::class.java)
                        note.id = doc.id
                        notesList.add(note)
                    }
                }

                mAdapter = MyAdapter(notesList, applicationContext, firestoreDB!!)
                rvNoteList.adapter = mAdapter
            })
    }

    override fun onDestroy() {
        super.onDestroy()

        firestoreListener!!.remove()
    }

    private fun loadNotesList() {
        firestoreDB!!.collection("notes")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val notesList = mutableListOf<Stuff>()

                    for (doc in task.result!!) {
                        val note = doc.toObject<Stuff>(Stuff::class.java)
                        note.id = doc.id
                        notesList.add(note)
                        runAnimation(rvNoteList!!)

                    }

                    mAdapter = MyAdapter(notesList, applicationContext, firestoreDB!!)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
                    rvNoteList.layoutManager = mLayoutManager
                    rvNoteList.itemAnimator = DefaultItemAnimator()
                    rvNoteList.adapter = mAdapter
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }

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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                var newText = newText
                newText = newText.toLowerCase()
                val newList = arrayListOf<Stuff>()
                for (stuff in notesList) {
                    val stuffTitle = stuff.title?.toLowerCase()
                    val stuffModel = stuff.model?.toLowerCase()
                    val stuffPrice = stuff.price?.toString()?.toLowerCase()
                    val stuffCount = stuff.count?.toString()?.toLowerCase()
                    val stuffDescription = stuff.description?.toLowerCase()

                    if (stuffTitle!!.contains(newText) || stuffModel!!.contains(newText) || stuffPrice!!.contains(newText) || stuffCount!!.contains(newText) || stuffDescription!!.contains(newText)) {
                        newList.add(stuff)
                    }
                }
                mAdapter?.setFilter(newList)
                return true
            }
        })



        return super.onCreateOptionsMenu(menu)
    }

    private fun sortListByTitle() {

        firestoreListener = firestoreDB!!.collection("notes").orderBy("title", Query.Direction.ASCENDING)
            .addSnapshotListener(EventListener { documentSnapshots, e ->

                val notesList = mutableListOf<Stuff>()

                Log.v(TAG, notesList.toString())
                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val note = doc.toObject(Stuff::class.java)
                        note.id = doc.id
                        notesList.add(note)
                    }
                }

                mAdapter = MyAdapter(notesList, applicationContext, firestoreDB!!)
                rvNoteList.adapter = mAdapter
            })
    }
    private fun sortListByPrice() {

        firestoreListener = firestoreDB!!.collection("notes").orderBy("price", Query.Direction.ASCENDING)
            .addSnapshotListener(EventListener { documentSnapshots, e ->

                val notesList = mutableListOf<Stuff>()

                Log.v(TAG, notesList.toString())
                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val note = doc.toObject(Stuff::class.java)
                        note.id = doc.id
                        notesList.add(note)
                    }
                }

                mAdapter = MyAdapter(notesList, applicationContext, firestoreDB!!)
                rvNoteList.adapter = mAdapter
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}