package com.gtp01.group01.android.recipesmobileapp.data

import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import com.gtp01.group01.android.recipesmobileapp.shared.models.NutritionModel

/**
 * Utility object providing methods for generating test data related to recipes.
 */
object RecipesData {
    /**
     * Retrieves a list of dummy nutrition models for testing purposes.
     * @return A list of NutritionModel objects representing dummy nutrition data.
     */
    fun getNutritionModelListData(): List<NutritionModel> {
        return listOf<NutritionModel>(
            NutritionModel(
                name = "flour",
                calories = 725.2,
                servingSizeG = 200.0,
                fatTotalG = 2.0,
                fatSaturatedG = 0.3,
                proteinG = 20.5,
                sodiumMg = 4,
                potassiumMg = 212,
                cholesterolMg = 0,
                carbohydratesTotalG = 153.9,
                fiberG = 5.4,
                sugarG = 0.5,
            ),
            NutritionModel(
                name = "sugar",
                calories = 1927.9,
                servingSizeG = 500.0,
                fatTotalG = 0.0,
                fatSaturatedG = 0.0,
                proteinG = 0.0,
                sodiumMg = 4,
                potassiumMg = 0,
                cholesterolMg = 0,
                carbohydratesTotalG = 499.1,
                fiberG = 0.0,
                sugarG = 500.3,
            )
        )
    }

    /**
     * Retrieves a list of original food categories for testing purposes.
     * @return A list of FoodCategory objects representing original food categories.
     */
    fun getOriginalCategoriesListData(): List<FoodCategory> {
        return listOf<FoodCategory>(
            FoodCategory(
                idFoodCategory = 1,
                categoryName = "Bread"
            ),
            FoodCategory(
                idFoodCategory = 2,
                categoryName = "Breakfast"
            )
        )
    }

    /**
     * Retrieves a list of selected food categories for testing purposes.
     * @return A list of FoodCategory objects representing selected food categories.
     */
    fun getSelectedCategoriesListData(): List<FoodCategory> {
        return listOf<FoodCategory>(
            FoodCategory(
                idFoodCategory = 1,
                categoryName = "Bread"
            ),
            FoodCategory(
                idFoodCategory = 2,
                categoryName = "Breakfast"
            )
        )
    }

    /**
     * Generates a dummy recipe data for testing purposes.
     * @return A Recipe object representing dummy recipe data.
     */
    fun getSavingRecipeData(): Recipe {
        return Recipe(
            owner = getUserData(),
            recipeName = "Test Recipe",
            ingredients = getIngredientsDataForSavingApi().toString(),
            instruction = getInstructionsDataForSavingApi(),
            preparationTime = 10,
            calorie = 725,
            protein = 21,
            carbs = 154,
            serving = 2,
            photo = null,
            categories = getSelectedCategoriesListData(),
            isActive = 1,
            likeCount = 0,
            hasLike = false,
            hasFavorite = false,
        )
    }

    /**
     * Retrieves a saved recipe data for testing purposes.
     * @return A Recipe object representing saved recipe data.
     */
    fun getSavedRecipeData(): Recipe {
        return Recipe(
            owner = getUserData(),
            recipeName = "Test Recipe",
            ingredients = getIngredientsDataForSavingApi().toString(),
            instruction = getInstructionsDataForSavingApi(),
            preparationTime = 10,
            calorie = 725,
            protein = 21,
            carbs = 154,
            serving = 2,
            photo = null,
            categories = getSelectedCategoriesListData(),
            isActive = 1,
            likeCount = 0,
            hasLike = false,
            hasFavorite = false,
        )
    }

    /**
     * Generates dummy instruction data for saving a recipe.
     * @return A string representing dummy instruction data.
     */
    fun getInstructionsDataForSavingApi(): String {
        return "Step 01: Add flour to the bowl.\nStep 02: Add water and mix well."
    }

    /**
     * Generates dummy ingredient data for saving a recipe.
     * @return A string representing dummy ingredient data.
     */
    fun getIngredientsDataForSavingApi(): String {
        return "200g of flour\n2 cups of sugar"
    }

    /**
     * Retrieves dummy ingredient data for nutrient API.
     * @return A string representing dummy ingredient data suitable for nutrient API.
     */
    fun getIngredientsDataForNutrientsApi(): String {
        return "200g of flour and 2 cups of sugar"
    }

    /**
     * Retrieves dummy user data for testing purposes.
     * @return A User object representing dummy user data.
     */
    fun getUserData(): User {
        return User(idUser = 10)
    }
}