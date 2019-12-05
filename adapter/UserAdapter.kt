package com.example.vlad.deliveryinstantapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.firebase.firestore.FirebaseFirestore


import android.content.Intent
import android.view.animation.AnimationUtils
import android.widget.*
import com.example.vlad.deliveryinstantapp.ItemActivity

import com.example.vlad.deliveryinstantapp.OrderActivity
import com.example.vlad.deliveryinstantapp.R
import com.example.vlad.deliveryinstantapp.model.Stuff
import com.squareup.picasso.Picasso

class UserAdapter(
    private var notesList: MutableList<Stuff>,
    private val context: Context,
    private val firestoreDB: FirebaseFirestore)
    : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item_delivery, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]

        holder!!.title.text = note.title
        holder!!.model.text = "Марка: " +note.model
        holder!!.price.text = "Ціна: " +note.price.toString()
        holder!!.count.text = "Кількість: " +note.count.toString()
        holder!!.description.text = "Опис: " +note.description
        holder!!.rating.rating = note.rating!!.toFloat()
        Picasso.with(holder.image.context)
            .load("https://firebasestorage.googleapis.com/v0/b/deliveryinstantapp.appspot.com/o/images%2F"+note.image+"?alt=media")
            .placeholder(android.R.drawable.alert_light_frame)
            .error(android.R.drawable.alert_dark_frame)
            .into(holder!!.image)

        holder.order.setOnClickListener { orderNote(note) }
        holder.details.setOnClickListener {openDetails(note)}

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
        internal var title: TextView
        internal var model: TextView
        internal var price: TextView
        internal var count: TextView
        internal var description: TextView
        internal var rating: RatingBar
        internal var image: ImageView
        internal var details: Button
        internal var order: ImageView


        init {
            title = view.findViewById(R.id.tvTitle)
            model = view.findViewById(R.id.tvModel)
            price = view.findViewById(R.id.tvPrice)
            count = view.findViewById(R.id.tvCount)
            description = view.findViewById(R.id.tvDescription)
            rating = view.findViewById(R.id.ratingBar)
            image  = view.findViewById(R.id.ivItem)
            details = view.findViewById(R.id.gotoItem)
            order = view.findViewById(R.id.ivOrder)


        }
    }

    private fun orderNote(note: Stuff) {
        val intent = Intent(context, OrderActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("UpdateNoteId", note.id)
        intent.putExtra("UpdateNoteTitle", note.title)
        intent.putExtra("UpdateNoteModel", note.model)
        intent.putExtra("UpdateNotePrice", note.price.toString())
        intent.putExtra("UpdateNoteCount", note.count.toString())
        intent.putExtra("UpdateNoteDescription", note.description)
        intent.putExtra("UpdateNoteRating", note.rating)
        intent.putExtra("UpdateNoteImage", note.image)


        context.startActivity(intent)
    }
    private fun openDetails(note: Stuff) {
        val intent = Intent(context, ItemActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("UpdateNoteId", note.id)
        intent.putExtra("UpdateNoteTitle", note.title)
        intent.putExtra("UpdateNoteModel", note.model)
        intent.putExtra("UpdateNotePrice", note.price.toString())
        intent.putExtra("UpdateNoteCount", note.count.toString())
        intent.putExtra("UpdateNoteDescription", note.description)
        intent.putExtra("UpdateNoteRating", note.rating)
        intent.putExtra("UpdateNoteImage", note.image)


        context.startActivity(intent)
    }

    fun setFilter(newList: List<Stuff>) {
        notesList = arrayListOf()
        notesList.addAll(newList)
        notifyDataSetChanged()
    }


}