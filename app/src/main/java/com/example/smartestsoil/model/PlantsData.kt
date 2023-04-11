package com.example.smartestsoil.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

object PlantsLibrary {
    var plantList: Map<String, List<PlantModel>>? = null

    fun getPlantList(context: Context): Map<String, List<PlantModel>> {
        if (plantList == null) {
            plantList = loadPlantList(context)
        }
        return plantList!!
    }

    fun loadPlantList(context: Context): Map<String, List<PlantModel>> {
        val jsonString = context.assets.open("plantslib.json").bufferedReader().use { it.readText() }
        val jsonParser = JsonParser()
        val jsonObject = jsonParser.parse(jsonString).asJsonObject

        val popularHouseplantsJsonArray = jsonObject.getAsJsonArray("popularHouseplants")
        val edibleHouseplantsJsonArray = jsonObject.getAsJsonArray("edibleHouseplants")
        val demandingHouseplantsJsonArray = jsonObject.getAsJsonArray("demandingHouseplants")

        val gson = Gson()
        val typeToken = object : TypeToken<List<PlantModel>>() {}.type

        val popularHouseplants = gson.fromJson<List<PlantModel>>(popularHouseplantsJsonArray, typeToken)
        val edibleHouseplants = gson.fromJson<List<PlantModel>>(edibleHouseplantsJsonArray, typeToken)
        val demandingHouseplants = gson.fromJson<List<PlantModel>>(demandingHouseplantsJsonArray, typeToken)

        val plantCategories = mapOf(
            "popularHouseplants" to popularHouseplants,
            "edibleHouseplants" to edibleHouseplants,
            "demandingHouseplants" to demandingHouseplants
        )

        return plantCategories
    }
}


/*object PlantsLibrary {
    var plantList: List<PlantModel>? = null
    fun getPlantList(context: Context): List<PlantModel> {
        if (plantList == null) {
            plantList = loadPlantList(context)
        }
        return plantList!!
    }
    fun loadPlantList(context: Context): List<PlantModel> {
        val jsonString =
            context.assets.open("plantslib.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, object : TypeToken<List<PlantModel>>() {}.type)
    }
}*/

data class PlantModel(
    val plantName: String,
    val care: CareModel,
    val suitableMoisture: MoistureModel,
    val suitableTemperature: TemperatureModel,
    val description: String
)

data class CareModel(
    val light: String,
    val water: String,
    val fertilizer: String
)

data class MoistureModel(
    val min: Int,
    val max: Int
)

data class TemperatureModel(
    val min: Int,
    val max: Int
)