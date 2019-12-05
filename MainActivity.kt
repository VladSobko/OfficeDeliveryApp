package com.example.vlad.deliveryinstantapp

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.widget.FrameLayout
import com.example.vlad.deliveryinstantapp.model.User

class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var menu_start_work: FrameLayout? = null
    private var button_menu: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        menu_start_work = findViewById<FrameLayout>(R.id.menu_start_work)
        button_menu = findViewById(R.id.main_button)
        button_menu!!.setOnClickListener{
            menu_start_work!!.visibility = View.INVISIBLE
        }

        val drawable = GradientDrawable()
        drawable.setColor(getColor(R.color.item_btn))
        drawable.cornerRadius = resources.getDimension(R.dimen.radius) // пример 16dp

        buttonSignIn.background = drawable
        buttonSignUp.background = drawable
        buttonLoginActivity.background = drawable

        // Click listeners
        buttonSignIn.setOnClickListener(this)
        buttonSignUp.setOnClickListener(this)
        buttonLoginActivity.setOnClickListener(this)

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                // show password
                fieldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                // hide password
                fieldPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        if (auth.currentUser != null)
            onLoginSuccess()

    }

    private fun signIn() {
        Log.d(TAG, "signIn")
        if (!validateForm()) {
            return
        }

        showProgressDialog()
        val email = fieldEmail.text.toString()
        val password = fieldPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                Log.d(TAG, "signIn:onComplete:" + task.isSuccessful)
                hideProgressDialog()

                if (task.isSuccessful) {
                    onLoginSuccess()


                } else {
                    Toast.makeText(
                        baseContext, "Sign In Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun signUp() {
        Log.d(TAG, "signUp")
        if (!validateForm()) {
            return
        }

        showProgressDialog()
        val email = fieldEmail.text.toString()
        val password = fieldPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                Log.d(TAG, "createUser:onComplete:" + task.isSuccessful)
                hideProgressDialog()

                if (task.isSuccessful) {
                    onRegisterSuccess(task.result?.user!!)
                } else {
                    Toast.makeText(
                        baseContext, "Sign Up Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun onLoginSuccess() {
        readUserFromDB()
    }

    private fun readUserFromDB() {

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user!!.uid
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val role = dataSnapshot.child("role").value.toString().toBoolean()
                val ban = dataSnapshot.child("ban").value.toString().toBoolean()
                if (!ban) {
                    if (role) {
                        val intent = Intent(this@MainActivity, ChooseActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@MainActivity, UserActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        baseContext, "Ви не  можете увійти, бо ви заблоковані",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }

    private fun onRegisterSuccess(user: FirebaseUser) {
        val username = usernameFromEmail(user.email!!)

        writeNewUser(user.uid, username, user.email, false, false)

    }

    private fun writeNewUser(userId: String, name: String, email: String?, role: Boolean?, ban: Boolean?) {
        val user = User(userId, name, email, role, ban)
        database.child("users").child(userId).setValue(user).addOnCompleteListener {
            startActivity(Intent(this@MainActivity, UserActivity::class.java))
            finish()
        }
    }


    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.buttonSignIn) {
            signIn()
        } else if (i == R.id.buttonSignUp) {
            signUp()
        }
        else if (i == R.id.buttonLoginActivity){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            //finish()
        }
    }


    private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            email
        }
    }

    private fun validateForm(): Boolean {
        var result = true
        if (TextUtils.isEmpty(fieldEmail.text.toString())) {
            fieldEmail.error = "Required"
            result = false
        } else {
            fieldEmail.error = null
        }

        if (TextUtils.isEmpty(fieldPassword.text.toString())) {
            fieldPassword.error = "Required"
            result = false
        } else {
            fieldPassword.error = null
        }

        return result
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}





