package com.example.smartestsoil.viewModel


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.smartestsoil.model.PlantModel
import com.example.smartestsoil.model.PlantsLibrary


class PlantListViewModel : ViewModel() {

    var plantList: Map<String, List<PlantModel>> = mapOf()
        private set

    fun getPlantList(context: Context, category: String): List<PlantModel> {
        if (plantList.isEmpty()) {
            plantList = PlantsLibrary.getPlantList(context)
        }
        return plantList[category] ?: emptyList()
    }
}
