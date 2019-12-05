package com.example.vlad.deliveryinstantapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.vlad.deliveryinstantapp.model.Stuff

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add.*

import java.io.IOException
import java.util.*

class AddActivity : AppCompatActivity() {

    private val TAG = "AddNoteActivity"

    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""
    var randomUid : String? = null

    lateinit var btnChoose: Button
    lateinit var btnUpload: Button
    lateinit var imageView: ImageView
    lateinit var filePath: Uri
    private var PICK_IMAGE_REQUEST = 71

    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        firestoreDB = FirebaseFirestore.getInstance()

        btnChoose = findViewById(R.id.btnChoose)
        btnUpload = findViewById(R.id.btnUpload)
        imageView = findViewById(R.id.imgView)


        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        btnChoose.setOnClickListener {
            chooseImage()
        }

        btnUpload.setOnClickListener {
            uploadImage()
        }

        val drawable = GradientDrawable()
        drawable.setColor(getColor(R.color.item_btn))
        drawable.cornerRadius = resources.getDimension(R.dimen.radius) // пример 16dp

        btnChoose.background = drawable
        btnUpload.background = drawable


        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("UpdateNoteId")

            edtTitle.setText(bundle.getString("UpdateNoteTitle"))
            edtModel.setText(bundle.getString("UpdateNoteModel"))
            edtPrice.setText(bundle.getString("UpdateNotePrice"))
            edtCount.setText(bundle.getString("UpdateNoteCount"))
            edtDescription.setText(bundle.getString("UpdateNoteDescription"))
            ratingBar.rating = bundle.getInt("UpdateNoteRating").toFloat()
            //UpdateNoteRating
            Picasso.with(imgView.context)
                .load("https://firebasestorage.googleapis.com/v0/b/deliveryinstantapp.appspot.com/o/images%2F"+bundle.getString("UpdateNoteImage")+"?alt=media")
                .placeholder(android.R.drawable.alert_light_frame)
                .error(android.R.drawable.alert_dark_frame)
                .into(imgView)

            btAdd.text = "Update"

        }

        btAdd.setOnClickListener {
            val title = edtTitle.text.toString()
            val model = edtModel.text.toString()
            val price = edtPrice.text.toString()
            val count = edtCount.text.toString()
            val description = edtDescription.text.toString()



            var image = randomUid
                Log.e(TAG, "$image STORAGE REFERENCE!")


            if (title.isNotEmpty()) {
                if (id.isNotEmpty()) {

                    updateNote(id, title, model, price.toDouble(), count.toInt(), description, ratingBar.rating.toInt(), image!!)
                } else {
                    addNote(title, model, price.toDouble(), count.toInt(), description, ratingBar.rating.toInt(), image!!)
                }
            }

            finish()
        }
    }

    private fun uploadImage() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()
         randomUid = UUID.randomUUID().toString()

        val ref = storageReference!!.child("images/" + randomUid.toString())
        ref.putFile(filePath)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Log.e(TAG, randomUid.toString()+"  STORAGE REFERENCE!")
                Toast.makeText(this@AddActivity, "Uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this@AddActivity, "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                    .totalByteCount
                progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
            }
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun updateNote(
        id: String,
        title: String,
        model: String,
        price: Double,
        count: Int,
        description: String,
        rating: Int,
        image: String
    ) {
        val note = Stuff(id, title, model, price, count, description, rating, image).toMap()

        firestoreDB!!.collection("notes")
            .document(id)
            .set(note)
            .addOnSuccessListener {
                Log.e(TAG, "Note document update successful!")
                Toast.makeText(applicationContext, "Note has been updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding Note document", e)
                Toast.makeText(applicationContext, "Note could not be updated!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addNote(title: String, model: String, price: Double, count: Int, description: String, rating: Int, image: String) {
        val note = Stuff(title, model, price, count, description, rating, image).toMap()

        firestoreDB!!.collection("notes")
            .add(note)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                Toast.makeText(applicationContext, "Note has been added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding Note document", e)
                Toast.makeText(applicationContext, "Note could not be added!", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}

