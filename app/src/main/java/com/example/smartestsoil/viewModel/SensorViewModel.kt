package com.example.smartestsoil.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartestsoil.model.SensorApi
import com.example.smartestsoil.model.SensorData
import com.example.smartestsoil.model.UserPlant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.patrykandpatrick.vico.core.extension.setFieldValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SensorViewModel : ViewModel() {

    var sensordata = mutableListOf<SensorData>()
        private set

    init {
        getSensorDataList()
    }

    private fun getSensorDataList() {
        viewModelScope.launch {
            var sensorApi: SensorApi? = null
            try {
                sensorApi = SensorApi!!.getInstance()
                sensordata.clear()
                sensordata.addAll(sensorApi.getSensorData())
            } catch (e: Exception) {
                Log.d("SENSORVIEWMODEL", e.message.toString())
            }
        }
    }

    fun getPlantO(plantId: String){
        viewModelScope.launch{
            CurrentPlant = getPlant(plantId)
        }
    }

    fun setPlantO(plant: UserPlant){
        viewModelScope.launch {
            setPlant(plant)
        }
    }

    fun deletePlantO(plantId: String){
        viewModelScope.launch {
            delPlant(plantId)
        }
    }

    private suspend fun setPlant(plant: UserPlant){
        val db = Firebase.firestore
        val plants = db.collection("plants")
        val plantCol = plants.whereEqualTo("plantId", plant.plantId).get().await()
        if(plantCol.count() == 1 ) {
            var thePlant = plantCol.documents[0].reference
            thePlant.update(mapOf("plantName" to plant.plantName,
                                  "pairedSensor" to plant.pairedSensor,
                                  "imageUrl" to plant.imageUrl))
        }
    }

    private suspend fun delPlant(plantId: String){
        val db = Firebase.firestore
        val plants = db.collection("plants")
        val plantCol = plants.whereEqualTo("plantId", plantId).get().await()
        for(doc in plantCol){
            doc.reference.delete()
                .addOnSuccessListener { Log.d(TAG, "plantId successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting plantId ", e) }
        }
    }
    private suspend fun getPlant(plantId: String): UserPlant {
        if(plantId == ""){
            throw Exception("trying to fetch null plant id")
        }
        try {
            val db = Firebase.firestore
            val plants = db.collection("plants")
                .whereEqualTo("plantId", plantId)
                .get()
                .await()

            if (plants.count() > 0) {
                var firstPlant = plants.documents[0]
                val url = firstPlant.get("imageUrl") ?: "no image"
                val pSen = firstPlant.get("pairedSensor") ?: ""
                val name = firstPlant.get("plantName") ?: "no plant"
                val uId = firstPlant.get("userId") ?: "no user"
                Log.d(TAG, "Fetched plant data: $plantId")
                return UserPlant(url as String, pSen as String, plantId, name as String, uId as String)

            } else {
                // Log when the document does not exist
                Log.w(TAG, "Plant not found: $plantId")
                throw Exception("Not found")
            }
        } catch (e: Exception) {
            // Handle exceptions, e.g. log the error or show a message to the user
            Log.e(TAG, "Error fetching plant data", e)
            throw e
        }
    }


    companion object {
        private var currentPlant: UserPlant? = null
        var CurrentPlant: UserPlant?
        get(){
            return currentPlant
        }
        set(value){
            currentPlant = value
        }

        private var currentSensor: String? = ""
        var CurrentSensor: String?
            get() {
                return currentSensor
            }
            set(value) {
                currentSensor = value
            }
        private var currentPlantId: String? = ""
        var CurrentPlantId: String?
            get() {
                return currentPlantId
            }
            set(value) {
                currentPlantId = value
            }

    }
}
