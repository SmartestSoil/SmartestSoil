package com.example.smartestsoil.viewModel


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.smartestsoil.database.PlantsLibrary
import com.example.smartestsoil.model.PlantModel

class PlantListViewModel : ViewModel() {

    fun getPlantList(context: Context): List<PlantModel> {
        return PlantsLibrary.getPlantList(context)
    }
}
