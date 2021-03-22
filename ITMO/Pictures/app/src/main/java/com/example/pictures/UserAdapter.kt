package com.example.pictures

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.list_item.view.*
import java.net.URL

class UserAdapter(
    private val memoryCache: LruCache<String, Bitmap>,
    private val items: List<Item>,
    private val onClick: (Item) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val holder = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
        holder.root.setOnClickListener {
            onClick(items[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size

    inner class UserViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(item: Item) {
            root.image_item.setImageBitmap(memoryCache.get(item.getMinImage()))
            /*val bitmap: Bitmap? = memoryCache.get(item.getMinImage())
            if (memoryCache.get(item.getMinImage()) == null) {
                root.image_item.visibility =ImageView.INVISIBLE
                Task(root, memoryCache).execute(item.getMinImage())
            } else {
                root.image_item.setImageBitmap(bitmap)
            }*/
        }
    }

    /*class Task(val root: View, val memoryCache: LruCache<String, Bitmap>) :
        AsyncTask<String, Unit, Bitmap>() {
        override fun doInBackground(vararg params: String?): Bitmap {
            val bitmap: Bitmap = BitmapFactory.decodeStream(URL(params[0]).openStream())
            memoryCache.put(params[0], bitmap)
            return bitmap
        }

        override fun onPostExecute(result: Bitmap) {
            root.image_item.setImageBitmap(result)
            root.image_item.visibility =ImageView.VISIBLE
            super.onPostExecute(result)
        }
    }*/
}