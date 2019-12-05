package com.example.vlad.deliveryinstantapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.*
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import android.util.Base64
import com.example.vlad.deliveryinstantapp.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    //lateinit var providers : List<AuthUI.IdpConfig>
    //private val MY_REQUEST_CODE: Int = 7117
    private val RC_SIGN_IN = 1822
    private lateinit var database: DatabaseReference

    @SuppressLint("PackageManagerGetSignatures")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)

        try {
            val info = packageManager.getPackageInfo(
                "com.example.vlad.deliveryinstantapp",
                GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))

            }
        } catch (e: NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {}



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                val username = usernameFromEmail(user!!.email!!)

                writeNewUser(user.uid, username, user.email, false, false)

                Toast.makeText(this,""+user.email, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, UserActivity::class.java)
                startActivity(intent)


            } else {
                val responce = IdpResponse.fromResultIntent(data)
                Toast.makeText(this,""+responce!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun writeNewUser(userId: String, name: String, email: String?, role: Boolean?, ban: Boolean?) {
        database = FirebaseDatabase.getInstance().reference
        val user = User(userId, name, email, role, ban)
        database.child("users").child(userId).setValue(user).addOnCompleteListener {
            startActivity(Intent(this@LoginActivity, UserActivity::class.java))
            finish()
        }
    }
    private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            email
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }



}
