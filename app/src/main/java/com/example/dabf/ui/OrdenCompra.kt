package com.example.dabf.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageButton
import com.example.dabf.R
import com.example.dabf.model.Ordenes
import android.content.Intent
import com.example.dabf.io.ApiService
import com.example.dabf.util.PreferenceHelper
import com.example.dabf.util.PreferenceHelper.get
import com.example.dabf.util.PreferenceHelper.set
import com.example.dabf.util.toast
import kotlinx.android.synthetic.main.activity_orders.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrdenCompra : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val ordenesadapter = OrdenesAdapter()

    lateinit var  btnBack: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        cargarOrdenes()
        /*val ordenes = ArrayList<Ordenes>()
        ordenes.add(Ordenes(1,"Express Courier","2199.99","Pending"))
        ordenes.add(Ordenes(2,"Express Courier","285.55","Recived"))
        ordenes.add(Ordenes(3,"Express Courier","94.65","Pending"))
        ordenes.add(Ordenes(4,"Express Courier","106.85","Recived"))*/

        rvProductos.layoutManager = LinearLayoutManager(this)
        rvProductos.adapter = ordenesadapter


        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            val intent = Intent(this@OrdenCompra,Dashboard::class.java )
            startActivity(intent)
            finish()
        }

    }
    private fun cargarOrdenes(){
        val token = preferences["token",""]
        val call = apiService.getOrdenes("Bearer $token")
        call.enqueue(object :Callback<ArrayList<Ordenes>>{
            override fun onResponse(
                call: Call<ArrayList<Ordenes>>,
                response: Response<ArrayList<Ordenes>>
            ) {

                if(response.isSuccessful){
                    response.body()?.let {
                        ordenesadapter.ordenes = it
                        ordenesadapter.notifyDataSetChanged()
                    }



                }

            }

            override fun onFailure(call: Call<ArrayList<Ordenes>>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })

    }
}