package com.example.vlad.deliveryinstantapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.example.vlad.deliveryinstantapp.R
import com.example.vlad.deliveryinstantapp.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class CartAdapter(private val notesList: MutableList<Order>,
                  private val context: Context,
                  private val firestoreDB: FirebaseFirestore
): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var onDoneClicked: ((Order, Boolean) -> Unit)? = null
    private var lastPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item_cart, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]

        holder.count.text = "Кількість: " +note.count.toString()
        holder.city.text = "Місто: " +note.city
        holder.street.text = "Вулиця: " +note.street
        holder.house.text = "Будинок: " +note.house
        holder.flat.text = "Квартира: " +note.flat
        holder.titleo.text = "Назва: " +note.titleo
        holder.modelo.text = "Марка: " +note.modelo
        holder.priceo.text = "Ціна: " +note.priceo.toString()
        Picasso.with(holder.imageo.context)
            .load("https://firebasestorage.googleapis.com/v0/b/deliveryinstantapp.appspot.com/o/images%2F"+note.imageo+"?alt=media")
            .placeholder(android.R.drawable.alert_light_frame)
            .error(android.R.drawable.alert_dark_frame)
            .into(holder.imageo)

        val sfd = SimpleDateFormat("dd.MM.yyyy")
        holder.date.text = "Дата заказу: " + sfd.format(note.date)
        holder.done.isChecked = note.done

        holder.done.setOnCheckedChangeListener{ _, isChecked ->
            onDoneClicked?.invoke(note, isChecked)

        }
        //holder!!.user.text = "Client email: " +note.user
        val animation = AnimationUtils.loadAnimation(
            context, if (position > lastPosition)
                R.anim.up_from_bottom
            else
                R.anim.down_from_top
        )
        holder.itemView.startAnimation(animation)
        lastPosition = position
    }


    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var count: TextView
        internal var city: TextView
        internal var street: TextView
        internal var house: TextView
        internal var flat: TextView
        internal var titleo: TextView
        internal var modelo: TextView
        internal var priceo: TextView
        internal var imageo: ImageView
        //internal var user: TextView
        internal var done: Switch
        internal var date: TextView

        init {
            count = view.findViewById(R.id.tvCount)
            city = view.findViewById(R.id.tvCity)
            street = view.findViewById(R.id.tvStreet)
            house = view.findViewById(R.id.tvHouse)
            flat = view.findViewById(R.id.tvFlat)
            titleo = view.findViewById(R.id.tvTitle)
            modelo = view.findViewById(R.id.tvModel)
            priceo = view.findViewById(R.id.tvPrice)
            imageo = view.findViewById(R.id.ivItem)
            //user = view.findViewById(R.id.tvClient)
            done = view.findViewById(R.id.switchDone)
            date = view.findViewById(R.id.tvDate)

        }
    }
}