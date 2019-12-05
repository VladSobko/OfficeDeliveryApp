package com.example.vlad.deliveryinstantapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import com.example.vlad.deliveryinstantapp.R
import com.example.vlad.deliveryinstantapp.model.Review
import com.google.firebase.firestore.FirebaseFirestore

class ReviewAdapter(private val notesList: MutableList<Review>,
                  private val context: Context,
                  private val firestoreDB: FirebaseFirestore
): RecyclerView.Adapter<ReviewAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item_review, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]

        holder.user.text = "Користувач: " +note.user
        holder.responce.text = "Відгук: " +note.text
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var user: TextView
        internal var responce: TextView


        init {
            user = view.findViewById(R.id.tvUser)
            responce = view.findViewById(R.id.tvResponce)


        }
    }

}