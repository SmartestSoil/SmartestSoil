package com.example.smartestsoil.database

import android.content.Context
import com.example.smartestsoil.model.PlantModel
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

object PlantsLibrary {
    var plantList: List<PlantModel>? = null

    fun getPlantList(context: Context): List<PlantModel> {
        if (plantList == null) {
            plantList = loadPlantList(context)
        }
        return plantList!!
    }

    fun loadPlantList(context: Context): List<PlantModel> {
        val jsonString = context.assets.open("plantsLib.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, object : TypeToken<List<PlantModel>>() {}.type)
    }
}