package com.example.contactapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ContactAdapter.ContactAdapterInterface {

    var url = "https://api.myjson.com/bins/152nus"
    lateinit var listUser: UserModel
    lateinit var adapter: ContactAdapter
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "contactApp"
    private val PREF_IS_LOGIN = "isLogin"
    private val PREF_NAME_PROFILE = "name"

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getData()
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        //Setup Recycler View
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val users = ArrayList<User>()
        users.add(User("Ryzki", "Febrian", 24))
        users.add(User("Iksan", "Sekuter",30))
        users.add(User("Ria", "Ricis", 25))
        users.add(User("Rafi", "Ahmad", 33))

        //Untuk Pindah ke halaman ContactForm
        tv_name.text = "Hallo ${sharedPref.getString(PREF_NAME_PROFILE, "")}"
        img_add.setOnClickListener {
            startActivity(Intent(this, ContactFormActivity::class.java))
        }
        img_logout.setOnClickListener {

            sharedPref.edit().apply {
                clear()
                apply()
            }
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }

    private fun getData(){
        val que = Volley.newRequestQueue(this)
        val req = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
            Log.d("error", it.toString())
            listUser = Gson().fromJson(it.toString(), UserModel::class.java)

            adapter = ContactAdapter(this, listUser.data)
            adapter.listener = this
            recycler_view.adapter = adapter
            progress.visibility = View.GONE
        }, Response.ErrorListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        })

        que.add(req)
    }

    override fun onDeleteClick(userData: UserData) {
        listUser.data.remove(userData)
        adapter.notifyDataSetChanged()
    }
}
