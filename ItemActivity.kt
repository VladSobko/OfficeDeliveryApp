package com.example.vlad.deliveryinstantapp

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.vlad.deliveryinstantapp.model.Stuff
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.activity_item.ratingBar
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.os.Parcelable
import java.lang.String.valueOf


class ItemActivity : AppCompatActivity() {

    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""
    private val TAG = "ItemActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        firestoreDB = FirebaseFirestore.getInstance()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        if (intent.getSerializableExtra("stuffObject") != null) {
            setStuffModel(intent.getSerializableExtra("stuffObject") as Stuff)
        }

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("UpdateNoteId")

            tvName.text = bundle.getString("UpdateNoteTitle")
            tvModel.text = "Марка: "+bundle.getString("UpdateNoteModel")
            tvPrice.text = "Ціна: "+bundle.getString("UpdateNotePrice")
            tvCount.text = "Кількість: "+bundle.getString("UpdateNoteCount")
            tvDescription.text = "Опис: "+bundle.getString("UpdateNoteDescription")
            ratingBar.rating = bundle.getInt("UpdateNoteRating",1).toFloat()
            Picasso.with(tvImage.context)
                .load("https://firebasestorage.googleapis.com/v0/b/deliveryinstantapp.appspot.com/o/images%2F"+bundle.getString("UpdateNoteImage")+"?alt=media")
                .placeholder(android.R.drawable.alert_light_frame)
                .error(android.R.drawable.alert_dark_frame)
                .into(tvImage)

        }
        val imageo = bundle.getString("UpdateNoteImage")

        btGoToOrd.setOnClickListener {

            val intent = Intent(this@ItemActivity, OrderActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("UpdateNoteId", id)
            intent.putExtra("UpdateNoteTitle",tvName.text)
            intent.putExtra("UpdateNoteModel", tvModel.text)
            intent.putExtra("UpdateNotePrice", tvPrice.text.toString())
            intent.putExtra("UpdateNoteCount", tvCount.text.toString())


            intent.putExtra("UpdateNoteImage", imageo)

            startActivity(intent)

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setStuffModel(note: Stuff?) {
        if (note == null) {
            return
        }
        //this.stuff = stuff
        tvName.text = "Назва: "+note.title
        tvModel.text = "Марка: "+note.model
        tvPrice.text = "Ціна: "+note.price.toString()
        tvCount.text = "Кількість: "+note.count.toString()
        tvDescription.text = "Опис: "+note.description.toString()

        Picasso.with(tvImage.context)
            .load("https://firebasestorage.googleapis.com/v0/b/deliveryinstantapp.appspot.com/o/images%2F"+note.image+"?alt=media")
            .placeholder(android.R.drawable.alert_light_frame)
            .error(android.R.drawable.alert_dark_frame)
            .into(tvImage)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
