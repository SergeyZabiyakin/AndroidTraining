package com.example.skynet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class UserAdapter(
    //private val memoryCache: LruCache<String, Bitmap>,
    private val items: List<Post>,
    private val onClick: (Post, UserViewHolder) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val holder = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
        holder.root.findViewById<ImageButton>(R.id.buttonCardView).setOnClickListener {
            onClick(items[holder.adapterPosition], holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int {

        return super.getItemViewType(position)
    }

    override fun getItemCount() = items.size

    inner class UserViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        val image = root.findViewById<ImageView>(R.id.imageItem)
        val titleItem = root.findViewById<TextView>(R.id.titleItem)
        val textItem = root.findViewById<TextView>(R.id.textItem)
        val buttonItem =root.findViewById<ImageButton>(R.id.buttonCardView)
        fun bind(item: Post) {
            if (!item.deleted) {
                image.visibility = ImageView.INVISIBLE
                titleItem.visibility = TextView.VISIBLE
                textItem.visibility = TextView.VISIBLE
                buttonItem.visibility=ImageButton.VISIBLE
                titleItem.text = item.title
                textItem.text = item.body
            } else {
                image.visibility = ImageView.VISIBLE
                titleItem.visibility = TextView.INVISIBLE
                textItem.visibility = TextView.INVISIBLE
                buttonItem.visibility=ImageButton.INVISIBLE
            }
        }
    }


}