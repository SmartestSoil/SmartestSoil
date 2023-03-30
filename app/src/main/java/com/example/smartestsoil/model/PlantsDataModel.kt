package com.example.smartestsoil.model

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
