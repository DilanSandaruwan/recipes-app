package com.gtp01.group01.android.recipesmobileapp.shared.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing nutrition information for a food item.
 *
 * @property name The name of the food item.
 * @property calories The number of calories per serving.
 * @property servingSizeG The serving size in grams.
 * @property fatTotalG The total amount of fat in grams.
 * @property fatSaturatedG The amount of saturated fat in grams.
 * @property proteinG The amount of protein in grams.
 * @property sodiumMg The amount of sodium in milligrams.
 * @property potassiumMg The amount of potassium in milligrams.
 * @property cholesterolMg The amount of cholesterol in milligrams.
 * @property carbohydratesTotalG The total amount of carbohydrates in grams.
 * @property fiberG The amount of dietary fiber in grams.
 * @property sugarG The amount of sugar in grams.
 */
data class NutritionModel(
    @SerializedName("name") val name: String,
    @SerializedName("calories") val calories: Double,
    @SerializedName("serving_size_g") val servingSizeG: Double,
    @SerializedName("fat_total_g") val fatTotalG: Double,
    @SerializedName("fat_saturated_g") val fatSaturatedG: Double,
    @SerializedName("protein_g") val proteinG: Double,
    @SerializedName("sodium_mg") val sodiumMg: Int,
    @SerializedName("potassium_mg") val potassiumMg: Int,
    @SerializedName("cholesterol_mg") val cholesterolMg: Int,
    @SerializedName("carbohydrates_total_g") val carbohydratesTotalG: Double,
    @SerializedName("fiber_g") val fiberG: Double,
    @SerializedName("sugar_g") val sugarG: Double,
)
