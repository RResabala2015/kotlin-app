package com.example.dabf.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.example.dabf.util.PreferenceHelper
import com.example.dabf.util.PreferenceHelper.set
import com.example.dabf.util.PreferenceHelper.get
import com.example.dabf.R
import com.example.dabf.io.ApiService
import com.example.dabf.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class Dashboard : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var  Logout : ImageButton
    lateinit var  Factura : ImageButton
    lateinit var btnlistar: ImageButton
    lateinit var check1: CheckBox
    lateinit var check2: CheckBox
    lateinit var check3: CheckBox
    lateinit var check4: CheckBox
    lateinit var precioAir: TextView
    lateinit var precioIph: TextView
    lateinit var precioIpad: TextView
    lateinit var precioAppW: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        precioAir = findViewById(R.id.etPrAirpods)
        precioIph = findViewById(R.id.etPrIph)
        precioIpad = findViewById(R.id.etPrIpad)
        precioAppW = findViewById(R.id.etPrWatch)



        check1 = findViewById(R.id.check1)
        check2 = findViewById(R.id.check2)
        check3 = findViewById(R.id.check3)
        check4 = findViewById(R.id.check4)

        Logout = findViewById(R.id.btnLogout)
        Logout.setOnClickListener{
            perfomLogout()
        }

        Factura = findViewById(R.id.btnComprar)
        Factura.setOnClickListener {
            var Airpods = check1?.isChecked
            var Iphone = check2?.isChecked
            var Ipad = check3?.isChecked
            var AppWatch = check4?.isChecked
            val p1 = precioAir.text.toString().toDouble()
            val p2 = precioIph.text.toString().toDouble()
            val p3 = precioIpad.text.toString().toDouble()
            val p4 = precioAppW.text.toString().toDouble()
            val id1 = "1"
            val pr1 = "Airpods Pro"
            val id2 = "2"
            val pr2 = "Iphone 13 Pro"
            val id3 = "3"
            val pr3 = "Ipad Pro"
            val id4 = "4"
            val pr4 = "Apple Watch"
            var ids = ""
            var total = 0.00
            var productsChoose =""


            if(Airpods== true){
                ids += id1 + ","
                productsChoose += pr1 + ", "
                total += p1.toString().toDouble()
            }
            if(Iphone== true){
                ids += id2 + ","
                productsChoose += pr2 + ", "
                total += p2.toString().toDouble()
            }
            if(Ipad== true){
                ids += id3 + ","
                productsChoose += pr3 + ", "
                total += p3.toString().toDouble()
            }
            if(AppWatch== true){
                ids += id4
                productsChoose += pr4
                total += p4.toString().toDouble()
            }

            TotalFactura("%.2f".format(total), ids, productsChoose);


        }

        btnlistar= findViewById(R.id.btnlistar)
        btnlistar.setOnClickListener{
            val intent = Intent(this@Dashboard, OrdenCompra::class.java )
            startActivity(intent)
        }
    }
    private fun perfomLogout(){
        val token =preferences["token",""]
        val call = apiService.postLogout("Bearer $token")
        call.enqueue(object :Callback<Void>{

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreference()

                val intent = Intent(this@Dashboard, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })

    }
    private fun clearSessionPreference(){
        val preferences =  PreferenceHelper.defaultPrefs(this)
        preferences["token"]=""
    }


    private fun TotalFactura(total:String, id:String, pr:String){
        val intent = Intent(this@Dashboard, FacturaDetalle::class.java)
        intent.putExtra("id", id)
        intent.putExtra("total", total)
        intent.putExtra("products", pr)
        startActivity(intent)
    }


}