package com.example.dabf.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dabf.R
import com.example.dabf.io.ApiService
import com.example.dabf.io.response.SignUpResponse
import com.example.dabf.util.PreferenceHelper
import com.example.dabf.util.PreferenceHelper.set
import com.example.dabf.util.toast
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import kotlin.jvm.Throws

class RegisterUser : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()

    }
    lateinit var txtNombre: EditText
    lateinit var txtApellido : EditText
    lateinit var txtCedula: EditText
    lateinit var txtTelefono: EditText
    lateinit var txtDireccion: EditText
    lateinit var txtCorreo: EditText
    lateinit var txtPass: EditText
    lateinit var btnGuardar: Button
    lateinit var btnback: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)


        txtNombre = findViewById(R.id.inNombres)
        txtApellido = findViewById(R.id.inApellidos)
        txtCedula= findViewById(R.id.inCedula)
        txtTelefono= findViewById(R.id.inTelefono)
        txtDireccion= findViewById(R.id.inDireccion)
        txtCorreo= findViewById(R.id.inCorreo)
        txtPass= findViewById(R.id.inContrasena)
        btnGuardar= findViewById(R.id.inGuardar)
        btnback = findViewById(R.id.btnBack)

        btnGuardar.setOnClickListener() {

            performRegistrer()

        }

        btnback.setOnClickListener(){

            val intent = Intent(this@RegisterUser, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
   }

    private fun performRegistrer() {

        val nombre = txtNombre.text.toString()
        val apellido = txtApellido.text.toString()
        val cedula =txtCedula.text.toString()
        val telefono = txtTelefono.text.toString()
        val direccion = txtDireccion.text.toString()
        val email = txtCorreo.text.toString()
        val  password = txtPass.text.toString()

        if (email.trim().isEmpty() || password.trim().isEmpty()
            || nombre.trim().isEmpty() || apellido.trim().isEmpty()
            || password.trim().isEmpty() || cedula.trim().isEmpty()
            || telefono.trim().isEmpty() || direccion.trim().isEmpty()){
            toast(getString(R.string.error_data_registrer))
            return
        }

        val call = apiService.postSignUp(cedula,nombre,apellido,telefono,direccion,email,password)
        call.enqueue(object :Callback<SignUpResponse>{
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: retrofit2.Response<SignUpResponse>
            ) {
               if (response.isSuccessful){
                   val signUpResponse = response.body()
                   if (signUpResponse == null) {
                       toast(getString(R.string.error_login_response))

                       return
                   }
                   if (signUpResponse.success) {
                       toast(getString(R.string.txt_mesaage_signup))
                        goToLogin()
                   }else{
                       toast("Usuario ya registrado, intentelo de nuevo")
                   }
               }else{
                   toast(getString(R.string.error_register_values))
               }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }
    private fun goToDashboardActivity(){
        val intent = Intent(this@RegisterUser, Dashboard::class.java)
        startActivity(intent)
        finish()
    }
    private fun goToLogin(){
        val intent = Intent(this@RegisterUser, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createSessionPreference(token: String){
        val preferences =  PreferenceHelper.defaultPrefs(this)
        preferences["token"]=token
    }

}