package com.example.smartestsoil.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartestsoil.model.SensorApi
import com.example.smartestsoil.model.SensorData
import kotlinx.coroutines.launch

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

    companion object {
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
                Log.d("in vm"," $currentPlantId")
            }

    }
}
