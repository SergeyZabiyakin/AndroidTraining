package com.sergey.mvvm.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sergey.mvvm.R
import com.sergey.mvvm.repository.db.Post
import java.util.*


class RecyclerViewAdapter(
    private val onClick: (Post) -> Unit
) :
    RecyclerView.Adapter<RecyclerViewAdapter.UserViewHolder>() {

    private val items = ArrayList<Post>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Post>) {
        items.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val holder = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
        holder.view.findViewById<ImageButton>(R.id.buttonCardView).setOnClickListener {
            onClick(items[holder.adapterPosition])
            //notifyItemChanged(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val titleItem = view.findViewById<TextView>(R.id.titleItem)
        private val textItem = view.findViewById<TextView>(R.id.textItem)
        fun bind(item: Post) {
            titleItem.text = item.title
            textItem.text = item.body
        }
    }

}