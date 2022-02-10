package com.example.dabf.ui
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.dabf.util.PreferenceHelper
import com.example.dabf.util.PreferenceHelper.get
import com.example.dabf.util.PreferenceHelper.set
import com.example.dabf.R
import com.example.dabf.io.ApiService
import com.example.dabf.io.response.LoginResponse
import com.example.dabf.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    //val session = true
    lateinit var  txtCorreo : EditText
    lateinit var  txtPass : EditText
    private lateinit var register: Button
    private lateinit var  Login : Button

    private val apiService:ApiService by lazy {
        ApiService.create()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences =  PreferenceHelper.defaultPrefs(this)
        if (preferences["token",""].contains("."))
            goToDashboardActivities()


        register  = findViewById(R.id.TxtRegister)
        Login = findViewById(R.id.btnLogin)

        txtCorreo = findViewById(R.id.etEmail)
        txtPass = findViewById(R.id.etPassword)

        Login.setOnClickListener{
            performLogin()

        }


        register.setOnClickListener{
            val intent = Intent(this@MainActivity, RegisterUser::class.java)
            startActivity(intent)

        }
    }

    private fun performLogin(){
        val email = txtCorreo.text.toString()
        val password = txtPass.text.toString()

        if(email.trim().isEmpty() || password.trim().isEmpty()){
            toast(getString(R.string.error_empty_credentials))
            return
        }
        val call = apiService.postLogin(email,password)
        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
               if(response.isSuccessful) {
                   val loginResponse = response.body()
                    if (loginResponse == null) {
                        toast(getString(R.string.error_login_response))

                        return
                    }
                    if (loginResponse.success) {
                        createSessionPreference(loginResponse.data.token)
                        toast("Bienvenido ${loginResponse.data.user.first_name}")
                        goToDashboardActivities()
                    } else {
                        toast(getString(R.string.error_invalid_credentials))
                    }
                }else{
                        toast(getString(R.string.error_login_response))

                    }
                }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }
    private fun createSessionPreference(token: String){
        println("----")
        println(token)
        println("----")
        val preferences =  PreferenceHelper.defaultPrefs(this)
        preferences["token"]=token
        println("*****")
        println(preferences["token",""])
        println("*****")
    }
    private fun goToDashboardActivities(){
        val intent = Intent(this@MainActivity, Dashboard::class.java)
       startActivity(intent)
        finish()
    }



}