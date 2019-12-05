package com.example.vlad.deliveryinstantapp.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.google.firebase.firestore.FirebaseFirestore


import android.content.Intent
import android.nfc.Tag
import android.util.Log
import android.widget.RatingBar

import android.widget.Toast
import com.example.vlad.deliveryinstantapp.AddActivity
import com.example.vlad.deliveryinstantapp.R
import com.example.vlad.deliveryinstantapp.model.Stuff
import com.squareup.picasso.Picasso
import java.io.File
import android.R.anim
import android.view.animation.AnimationUtils.loadAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils


class MyAdapter(
    private var notesList: MutableList<Stuff>,
    private val context: Context,
    private val firestoreDB: FirebaseFirestore)
    : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var lastPosition = -1
    private val TAG = "MyAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item, parent, false)

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
        Log.v(TAG, "https://firebasestorage.googleapis.com/v0/b/deliveryinstantapp.appspot.com/o/images%2F"+note.image+"?alt=media")
        Picasso.with(holder.image.context)
            .load("https://firebasestorage.googleapis.com/v0/b/deliveryinstantapp.appspot.com/o/images%2F"+note.image+"?alt=media")
            .placeholder(android.R.drawable.alert_light_frame)
            .error(android.R.drawable.alert_dark_frame)
            .into(holder!!.image)

        holder.edit.setOnClickListener { updateNote(note) }
        holder.delete.setOnClickListener { deleteNote(note.id!!, position) }

        val animation = loadAnimation(context, if (position > lastPosition)
            R.anim.up_from_bottom
        else
            R.anim.down_from_top)
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
        internal var edit: ImageView
        internal var delete: ImageView

        init {
            title = view.findViewById(R.id.tvTitle)
            model = view.findViewById(R.id.tvModel )
            price = view.findViewById(R.id.tvPrice)
            count = view.findViewById(R.id.tvCount)
            description = view.findViewById(R.id.tvDescription)
            rating = view.findViewById(R.id.ratingBar)
            image  = view.findViewById(R.id.ivItem)


            edit = view.findViewById(R.id.ivEdit)
            delete = view.findViewById(R.id.ivDelete)
        }
    }

    private fun updateNote(note: Stuff) {
        val intent = Intent(context, AddActivity::class.java)
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

    private fun deleteNote(id: String, position: Int) {

            firestoreDB.collection("notes")
                .document(id)
                .delete()
                .addOnCompleteListener {
                    notesList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, notesList.size)
                    Toast.makeText(context, "Note has been deleted!", Toast.LENGTH_SHORT).show()
                }

    }

    fun setFilter(newList: List<Stuff>) {
        notesList = arrayListOf()
        notesList.addAll(newList)
        notifyDataSetChanged()
    }
}