package com.example.vlad.deliveryinstantapp


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.Switch
import android.widget.Toast
import com.example.vlad.deliveryinstantapp.adapter.UserEditAdapter
import com.example.vlad.deliveryinstantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admin_user.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import android.R.dimen
import android.R.attr.radius
import android.R.color
import android.R.attr.colorAccent
import android.graphics.drawable.GradientDrawable
import android.R.id
import android.R.attr.button
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController


class AdminUserActivity : AppCompatActivity(){


    var mRecyclerView: RecyclerView? = null
    private lateinit var banSwitch: Switch
    private var ref = FirebaseDatabase.getInstance().reference

    val users = ArrayList<User>()
    val adapter = UserEditAdapter(users)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        populaltesList()

        val button = findViewById<View>(R.id.button_logout)
        val buttonrv = findViewById<View>(R.id.button_review)


        val drawable = GradientDrawable()
        drawable.setColor(getColor(R.color.colorPrimaryDark))
        drawable.cornerRadius = resources.getDimension(R.dimen.radius) // пример 16dp

        button.background = drawable
        buttonrv.background = drawable

        button_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@AdminUserActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT).show()
        }
        button_review.setOnClickListener {
            val intent = Intent(this@AdminUserActivity, ReviewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun populaltesList() {
        mRecyclerView = findViewById(R.id.recyclerViewau)

        val mDatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val mListenerPopulateList = mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for (convSnapshot in dataSnapshot.children) {
                    val conv = convSnapshot.getValue(User::class.java)
                    if (conv?.role == false)
                    users.add(conv)
                    runAnimation(mRecyclerView!!)
                }

                adapter.onBanClicked = { user, isChecked -> updateUser(user, isChecked) }
                mRecyclerView!!.adapter = adapter
                mRecyclerView!!.layoutManager = LinearLayoutManager(this@AdminUserActivity)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

             mDatabaseReference.addListenerForSingleValueEvent(mListenerPopulateList)
    }

    private fun runAnimation(mRecyclerView: RecyclerView) {
        val context : Context = mRecyclerView.context
        val controller: LayoutAnimationController?
        controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
        mRecyclerView.adapter = adapter

        mRecyclerView.layoutAnimation = controller
        adapter.notifyDataSetChanged()
        mRecyclerView.scheduleLayoutAnimation()

    }

    private fun updateUser(user: User, isBan: Boolean) {
        val userId = user.userId
        ref = FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("ban")
        ref.setValue(isBan)

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
                val newList = arrayListOf<User>()
                for (user in users) {
                    val userName = user.username?.toLowerCase()
                    val userEmail = user.email?.toLowerCase()

                    if (userName!!.contains(newText) || userEmail!!.contains(newText) ) {
                        newList.add(user)
                    }
                }
                adapter.setFilter(newList)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }



}

