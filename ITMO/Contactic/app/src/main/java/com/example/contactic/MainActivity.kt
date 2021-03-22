package com.example.contactic

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*

class MainActivity : AppCompatActivity() {
    lateinit var usersList: List<User>

    data class User(val name: String, val phoneNumber: String)

    lateinit var userPhone: String
    fun Context.fetchAllContacts(): List<User> {
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
            .use { cursor ->
                if (cursor == null) return emptyList()
                val builder = ArrayList<User>()
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            ?: "N/A"
                    val phoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            ?: "N/A"

                    builder.add(User(name, phoneNumber))
                }
                return builder
            }
    }

    class UserAdapter(
        private val users: List<User>,
        private val onClick: (User) -> Unit
    ) :
        RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val holder = UserViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            )
            holder.root.setOnClickListener {
                onClick(users[holder.adapterPosition])
            }
            return holder
        }

        override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) =
            holder.bind(users[position])

        override fun getItemCount() = users.size

        class UserViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
            fun bind(user: User) {
                with(root) {
                    first_name.text = user.name
                    last_name.text = user.phoneNumber
                }
            }
        }
    }

    fun init() {
        usersList = fetchAllContacts()
        val viewManaher = LinearLayoutManager(this@MainActivity)
        myRecyclerView.apply {
            layoutManager = viewManaher
            adapter = UserAdapter(usersList) {
                userPhone = it.phoneNumber
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    val sendIntent: Intent =
                        Intent(Intent.ACTION_DIAL, Uri.parse("tel: ${it.phoneNumber}"))
                    startActivity(sendIntent)
                }
            }
        }
        myRecyclerView.setHasFixedSize(true)
        /*val toast =*/ Toast.makeText(
            this@MainActivity,
            resources.getQuantityString(R.plurals.contacts, usersList.size, usersList.size),
            Toast.LENGTH_LONG
        ).show()
        //toast.setGravity(Gravity.AXIS_CLIP, 0, 60)
        //toast.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init()
                } else {
                    Toast.makeText(this@MainActivity, "No access to contacts", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val sendIntent: Intent =
                        Intent(Intent.ACTION_DIAL, Uri.parse("tel: ${userPhone}"))
                    startActivity(sendIntent)
                } else {
                    Toast.makeText(this@MainActivity, "No access to call phone", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                0
            )
        } else {
            init()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("phoneNumber", userPhone)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userPhone = savedInstanceState.getString("phoneNumber").toString()
    }
}