package com.example.hw10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class UserAdapter(
    val items : MutableList<Post>,
    private val onClick: (Post) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val holder = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
        holder.root.findViewById<ImageButton>(R.id.buttonCardView).setOnClickListener {
            onClick(items[holder.adapterPosition])
            notifyItemChanged(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount() = items.size

    inner class UserViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        val image = root.findViewById<ImageView>(R.id.imageItem)
        val titleItem = root.findViewById<TextView>(R.id.titleItem)
        val textItem = root.findViewById<TextView>(R.id.textItem)
        val buttonItem = root.findViewById<ImageButton>(R.id.buttonCardView)
        fun bind(item: Post) {
            if (!item.deleted) {
                image.visibility = View.GONE
                titleItem.visibility = View.VISIBLE
                textItem.visibility = View.VISIBLE
                buttonItem.visibility = View.VISIBLE
                titleItem.text = item.title
                textItem.text = item.body
            } else {
                image.visibility = View.VISIBLE
                titleItem.visibility = View.GONE
                textItem.visibility = View.GONE
                buttonItem.visibility = View.GONE
            }
        }
    }

}