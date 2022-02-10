package com.example.dabf.io

import com.example.dabf.model.Orders
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import com.example.dabf.io.response.LoginResponse
import com.example.dabf.io.response.OrdersResponse
import com.example.dabf.io.response.SignUpResponse
import com.example.dabf.model.Ordenes
import retrofit2.http.*
import okhttp3.logging.HttpLoggingInterceptor




interface ApiService {

    /*@GET("product")
    abstract fun getOrders(): Call<ArrayList<Orders>>*/


    @POST("login")
    fun postLogin(@Query("email")email:String,@Query("password")password:String):
        Call<LoginResponse>

  @POST("orders")
  @Headers("Accept:application/json")
  fun postOrders(@Header("Authorization")authHeader: String,
                 @Query("courier")courier:String,@Query("remarks")remarks:String,
                 @Query("products")products:String):
          Call<OrdersResponse>



    @POST("signup")
    @Headers("Accept:application/json")
    fun postSignUp(
        @Query("citizen_card")citizen:String,@Query("first_name")first_name:String,
        @Query("last_name")last_name:String,@Query("phone_number")phone_number:String,
        @Query("address")address:String,@Query("email")email:String,@Query("password")password:String):
            Call<SignUpResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization")authHeader: String): Call<Void>

    @GET("user/orders")
    fun getOrdenes(@Header("Authorization")authHeader: String):
            Call<ArrayList<Ordenes>>

    companion object Factory{
        private const val BASE_URL = "http://192.168.1.6:8000/api/"

        fun create(): ApiService{

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}