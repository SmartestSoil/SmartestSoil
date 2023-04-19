package com.example.smartestsoil.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


const val BASE_URL = "http://smartestsoil.duckdns.org:3000/"
interface SensorApi {
    @GET("/api/data")
    suspend fun getSensorData(): List<SensorData>
    companion object {
        private var SensorDataService : SensorApi? = null
        fun getInstance() : SensorApi {
            if (SensorDataService === null) {
                SensorDataService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create( SensorApi::class.java)
            }
            return  SensorDataService!!
        }
    }
}
data class SensorData(
    var id: Int,
    var timestamp: String,
    var sensor_id:String,
    var soil_moisture:Float,
    var soil_temperature:Float,

)