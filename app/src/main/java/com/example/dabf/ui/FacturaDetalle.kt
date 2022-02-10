package com.example.dabf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.dabf.R
import com.example.dabf.io.ApiService
import com.example.dabf.io.response.LoginResponse
import com.example.dabf.io.response.OrdersResponse
import com.example.dabf.io.response.SignUpResponse
import com.example.dabf.util.PreferenceHelper
import com.example.dabf.util.PreferenceHelper.get
import com.example.dabf.util.PreferenceHelper.set
import com.example.dabf.util.toast
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.io.println as println

class FacturaDetalle : AppCompatActivity() {

    private lateinit var btnBackfac: ImageButton
    lateinit var txtNomb: TextView
    lateinit var txtIDS: TextView
    private lateinit var txtApell: EditText
    private lateinit var txtDirec: EditText
    private lateinit var txtCourier: EditText
    private lateinit var txtObs: EditText
    private lateinit var btnPay: Button
    lateinit var txttotal: TextView

    private val apiService: ApiService by lazy {
        ApiService.create()

    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factura_detalle)
        //txtNomb = findViewById(R.id.txtNomb)
        txtCourier = findViewById(R.id.txtCourier)
        txtObs = findViewById(R.id.txtObs)
        txttotal = findViewById(R.id.txttotal)
        txtIDS = findViewById(R.id.txtIDS)

        /*val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val direccion = intent.getStringExtra("direccion")

        txtNomb.setText(nombre)
        txtApell.setText(apellido)
        txtDirec.setText(direccion)*/

        val products = intent.getStringExtra("products")
        txtIDS.setText(products)
        val total = intent.getStringExtra("total")
        txttotal.setText(total)



        btnBackfac = findViewById(R.id.btnBackfac)
        btnBackfac.setOnClickListener {

            goToDashboardActivity()
        }

        btnPay = findViewById(R.id.btnPay)
        btnPay.setOnClickListener {
            PerformPay()
        }


    }

    private fun PerformPay() {
        val token = preferences["token", ""]
        val courier = txtCourier.text.toString()
        val observaciones = txtObs.text.toString()
        val products = intent.getStringExtra("id").toString()

        if (courier.trim().isEmpty() || observaciones.trim().isEmpty()) {
            toast(getString(R.string.txt_failed_direc_ob))
            return
        }
        val call = apiService.postOrders("Bearer $token", courier, observaciones, products)
        call.enqueue(object : Callback<OrdersResponse> {
            override fun onResponse(
                call: Call<OrdersResponse>,
                response: Response<OrdersResponse>
            ) {
                toast("Orden Generada Correctamente")
                goToDashboardActivity()
                   if (response.isSuccessful) {
                    val OrdersResponse = response.body()
                    if (OrdersResponse == null) {
                        toast(getString(R.string.error_login_response))

                        return
                    }
                    if (OrdersResponse.success) {
                        toast("Orden Generada Correctamente")
                        goToDashboardActivity()


                    }
                } else {
                    toast(getString(R.string.str_ordenes_error))
                }
            }

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })

    }

    private fun goToDashboardActivity() {
        val intent = Intent(this@FacturaDetalle, Dashboard::class.java)
        startActivity(intent)
        finish()
        }

}