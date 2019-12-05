package com.example.vlad.deliveryinstantapp

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vlad.deliveryinstantapp.model.Order
import com.example.vlad.deliveryinstantapp.model.Review
import com.example.vlad.deliveryinstantapp.model.Stuff
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
//import com.example.vlad.deliveryinstantapp.viewmodel.StuffRecyclerViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order.*
import java.util.*
import kotlinx.android.synthetic.main.order_item.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import android.R.string.cancel
import android.content.DialogInterface
import android.R.drawable
import android.support.v7.app.AlertDialog




class OrderActivity : AppCompatActivity() {
    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""
    private val TAG = "OrderActivity"


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        firestoreDB = FirebaseFirestore.getInstance()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        if (intent.getSerializableExtra("stuffObject") != null) {
            setStuffModel(intent.getSerializableExtra("stuffObject") as Stuff)
        }

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("UpdateNoteId")

            tvName.text = bundle.getString("UpdateNoteTitle")
            tvModel.text = bundle.getString("UpdateNoteModel")
            tvPrice.text =bundle.getString("UpdateNotePrice")
            tvCount.text = bundle.getString("UpdateNoteCount")
            Picasso.with(tvImage.context)
                .load("https://firebasestorage.googleapis.com/v0/b/deliveryinstantapp.appspot.com/o/images%2F"+bundle.getString("UpdateNoteImage")+"?alt=media")
                .placeholder(android.R.drawable.alert_light_frame)
                .error(android.R.drawable.alert_dark_frame)
                .into(tvImage)

        }



        val c = Calendar.getInstance()
        val currentDate = Date()
        c.time = currentDate
        val dateFormatOrder = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(currentDate)
        textViewCurrentDate.text = dateFormatOrder.toString()

        c.add(Calendar.DATE, 3)
        val currentDatePlusThree = c.time
        val dateFormatDelivery = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(currentDatePlusThree)
        textViewDeliveryDate.text = dateFormatDelivery.toString()

        val format = SimpleDateFormat("dd.MM.yyyy")
        format.applyPattern("dd.MM.yyyy")

        btOrd.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val userEmail: String = user!!.email.toString()

            val count: Int = editCountText.text.toString().toInt()
            val countFromBase =bundle.getString("UpdateNoteCount").toInt()
            val date =  dateFormatOrder
            val city = editCityText.text.toString()
            val street = editStreetText.text.toString()
            val house = editHouseText.text.toString()
            val flat = editFlatText.text.toString()
            val titleo = bundle.getString("UpdateNoteTitle")
            val modelo = bundle.getString("UpdateNoteModel")
            val priceo = bundle.getString("UpdateNotePrice").toDouble()

            val imageo = bundle.getString("UpdateNoteImage")

            val responce = editTextReview.text.toString()



            if (editCityText.text.isEmpty() || editCityText.text.isNullOrEmpty()) {
                Toast.makeText(this, "Введіть місто, будь ласка!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            else
                if (editHouseText.text.isEmpty() || editHouseText.text.isNullOrEmpty()) {
                    Toast.makeText(this, "Введіть номер будинку, будь ласка!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                }
                else
                    if (editStreetText.text.isEmpty() || editStreetText.text.isNullOrEmpty()) {
                        Toast.makeText(this, "Введіть вулицю, будь ласка!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener

                    }
                    else
                        if (editCountText.text.isEmpty() || editCountText.text.isNullOrEmpty()) {
                            Toast.makeText(this, "Введіть кількість, будь ласка!", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener

                        }
                        else
                            if (editFlatText.text.isEmpty() || editFlatText.text.isNullOrEmpty()) {
                                Toast.makeText(this, "Введіть номер офісу, будь ласка!", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }
                            else if(count > countFromBase){
                                Toast.makeText(this, "Немає стільки, оберіть менше!", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }
                            else
                                if(editTextReview.text.isEmpty() || editTextReview.text.isNullOrEmpty()){
                                    updateCountWhenOrder()
                                    addOrderWithoutResponce(count, city, street, house, flat,titleo,modelo,priceo,imageo,userEmail,false,format.parse(dateFormatOrder + 2))
                                    val builder = AlertDialog.Builder(this@OrderActivity)
                                    builder.setTitle("Ви успішно замовили!Очікуйте доставку!")
                                        .setMessage("Не забудьте відмітити доставку в замовленнях, коли буде доставлено!")
                                        .setIcon(R.drawable.ic_info)
                                        .setCancelable(false)
                                        .setNegativeButton("ОК",
                                            DialogInterface.OnClickListener { dialog, id -> dialog.cancel()
                                            finish()
                                            })
                                    val alert = builder.create()
                                    alert.show()

                                }

                            else if(editTextReview.text.isNotEmpty()){
                                updateCountWhenOrder()
                                addOrder(count, city, street, house, flat,titleo,modelo,priceo,imageo,userEmail,false,format.parse(dateFormatOrder + 2),responce)
                                    val builder = AlertDialog.Builder(this@OrderActivity)
                                    builder.setTitle("Ви успішно замовили!Очікуйте доставку!")
                                        .setMessage("Не забудьте відмітити доставку в замовленнях, коли буде доставлено!")
                                        .setIcon(R.drawable.ic_info)
                                        .setCancelable(false)
                                        .setNegativeButton("ОК",
                                            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                    val alert = builder.create()
                                    alert.show()
                                        finish()
                            }
        }

    }

    private fun updateCountWhenOrder() {
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        val noteRef: DocumentReference = db.collection("notes").document(id)


        db.runTransaction { transaction ->
            val snapshot = transaction.get(noteRef)
            val newCount = snapshot.getDouble("count")!! - editCountText.text.toString().toInt()
            if (newCount >= 0) {
                transaction.update(noteRef, "count", newCount)
                newCount
            } else {
                throw FirebaseFirestoreException("Something wrong",
                    FirebaseFirestoreException.Code.ABORTED)
            }
        }.addOnSuccessListener { result ->
            Log.d(TAG, "Transaction success: ${result!!}")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Transaction failure.", e)
        }

    }

    private fun addOrder(count: Int, city: String, street: String, house: String, flat: String,titleo: String,modelo: String,priceo: Double,imageo: String, userEmail: String, done: Boolean,date:Date, responce: String) {

        val ord = Order(count, city, street, house, flat,titleo,modelo,priceo,imageo,userEmail,false,date).toMap()

        firestoreDB!!.collection("orders")
            .add(ord)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.id)



            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding Note document", e)
                Toast.makeText(applicationContext, "Order could not be added!", Toast.LENGTH_SHORT).show()
            }

        val resp = Review(responce,userEmail).toMap()

        firestoreDB!!.collection("reviews")
            .add(resp)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                Toast.makeText(applicationContext, "Review has been added!", Toast.LENGTH_SHORT).show()


            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding Note document", e)
                Toast.makeText(applicationContext, "Review could not be added!", Toast.LENGTH_SHORT).show()
            }


    }
    private fun addOrderWithoutResponce(count: Int, city: String, street: String, house: String, flat: String,titleo: String,modelo: String,priceo: Double,imageo: String, userEmail: String, done: Boolean,date:Date) {

        val ord = Order(count, city, street, house, flat,titleo,modelo,priceo,imageo,userEmail,false,date).toMap()

        firestoreDB!!.collection("orders")
            .add(ord)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                Toast.makeText(applicationContext, "Order has been added!", Toast.LENGTH_SHORT).show()


            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding Note document", e)
                Toast.makeText(applicationContext, "Order could not be added!", Toast.LENGTH_SHORT).show()
            }
    }


    @SuppressLint("SetTextI18n")
    private fun setStuffModel(note: Stuff?) {
        if (note == null) {
            return
        }
        //this.stuff = stuff
        tvName.text = note.title
        tvModel.text = note.model
        tvPrice.text = note.price.toString()
        tvCount.text = note.count.toString()
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
