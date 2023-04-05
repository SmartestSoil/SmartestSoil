package com.example.smartestsoil.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class UserSensor(
    var sensorname: String

)

const val BASE_URL_2="https://smartestsoil-default-rtdb.europe-west1.firebasedatabase.app/"


interface UserSensorsApi{
    @GET("userSensors")
    suspend fun getUserSensors():List<UserSensor>

    companion object{
        var userSensorsService:UserSensorsApi?= null

        fun getInstance():UserSensorsApi{
            if (userSensorsService===null){
                userSensorsService = Retrofit.Builder()
                    .baseUrl(BASE_URL_2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(UserSensorsApi::class.java)
            }
            return userSensorsService!!
        }
    }
}

fun addUserSensor(sensorname: String){
    val userId = Firebase.auth.currentUser!!.uid
    lateinit var database: DatabaseReference
    database = Firebase.database.reference
    val userSensor = UserSensor(sensorname)
    database.child("userInfo").child(userId).setValue(userSensor)
}