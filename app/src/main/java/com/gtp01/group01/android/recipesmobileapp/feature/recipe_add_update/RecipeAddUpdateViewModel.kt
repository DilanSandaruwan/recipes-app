package com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.constant.TagConstant
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategoryApp
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.models.NutritionModel
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.RecipeManagementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * ViewModel for adding or updating a recipe.
 *
 * @property recipeManagementRepository The repository for managing recipe data.
 * @property _nutritionList Internal mutable live data for nutrition information.
 * @property nutrition Live data for observing nutrition information changes.
 */
@HiltViewModel
class RecipeAddUpdateViewModel @Inject constructor(
    private val recipeManagementRepository: RecipeManagementRepository
) : ViewModel() {

    // ByteArray for storing the image of the recipe
    var imageBytes: ByteArray? = null

    // List to store calculated nutrients
    var calculatedNutrients = ArrayList<Int>()

    // List of editable food categories
    var editableCategoriesList: MutableList<FoodCategoryApp> = mutableListOf()

    // List of ingredients for the recipe
    var ingredientsList: MutableList<String> = mutableListOf()

    // List of instructions for the recipe
    var instructionsList: MutableList<String> = mutableListOf()

    // LiveData for serving count
    private val _serveCount = MutableLiveData(0)
    val serveCount: LiveData<Int> = _serveCount

    // LiveData for cooking time
    private val _cookingTime = MutableLiveData(0)
    val cookingTime: LiveData<Int> = _cookingTime

    // LiveData for nutrition information
    private val _nutritionList = MutableLiveData<List<NutritionModel>>()
    val nutritionList: LiveData<List<NutritionModel>> = _nutritionList

    // LiveData for category list
    private val _categoryList = MutableLiveData<List<FoodCategory>>()
    val categoryList: LiveData<List<FoodCategory>> = _categoryList

    // LiveData for successful recipe save
    private val _saveRecipeSuccess = MutableLiveData<Recipe?>()
    val saveRecipeSuccess: LiveData<Recipe?> = _saveRecipeSuccess

    /**
     * Fetches nutrition information based on the provided ingredients.
     *
     * @param ingredients The ingredients to fetch nutrition information for.
     */
    fun getNutritionsVm(ingredients: String) {
        viewModelScope.launch {
            val response = recipeManagementRepository.getNutritionsRepo(ingredients)
            _nutritionList.postValue(response ?: emptyList())
        }
    }

    /**
     * Fetches the list of food categories.
     */
    fun getCategoryList() {
        viewModelScope.launch {
            val response = recipeManagementRepository.getCategoryList()
            _categoryList.postValue(response ?: emptyList())
        }
    }

    /**
     * Saves a new recipe.
     *
     * @param idLoggedUser The ID of the logged-in user.
     * @param recipe The recipe to be saved.
     */
    fun saveRecipe(idLoggedUser: Int, recipe: Recipe) {
        viewModelScope.launch {
            val response = recipeManagementRepository.saveNewRecipe(idLoggedUser, recipe)
            if (response == null) {
                _saveRecipeSuccess.postValue(null)
            } else {
                _saveRecipeSuccess.postValue(response)
            }
        }
    }

    /**
     * Resets the serving count or cooking time to default value.
     *
     * @param tag The tag indicating whether to reset serving count or cooking time.
     */
    fun setDefaultCount(tag: Int) {
        when (tag) {
            TagConstant.TAG_SERVE_COUNT -> {
                _serveCount.value?.let {
                    _serveCount.postValue(0)
                }
            }

            TagConstant.TAG_COOKING_TIME -> {
                _cookingTime.value?.let {
                    _cookingTime.postValue(0)
                }
            }
        }
    }

    /**
     * Increases the serving count or cooking time by 1.
     *
     * @param tag The tag indicating whether to increase serving count or cooking time.
     */
    fun increaseCount(tag: Int) {
        when (tag) {
            TagConstant.TAG_SERVE_COUNT -> {
                _serveCount.value?.let { serveCount ->
                    _serveCount.postValue(serveCount.plus(1))
                }
            }

            TagConstant.TAG_COOKING_TIME -> {
                _cookingTime.value?.let { cookingTime ->
                    _cookingTime.postValue(cookingTime.plus(1))
                }
            }
        }
    }

    /**
     * Decreases the serving count or cooking time by 1.
     *
     * @param tag The tag indicating whether to decrease serving count or cooking time.
     */
    fun decreaseCount(tag: Int) {
        when (tag) {
            TagConstant.TAG_SERVE_COUNT -> {
                _serveCount.value?.let { serveCount ->
                    if (serveCount > 0) {
                        _serveCount.postValue(serveCount.minus(1))
                    } else {
                        _serveCount.postValue(0)
                    }
                }
            }

            TagConstant.TAG_COOKING_TIME -> {
                _cookingTime.value?.let { cookingTime ->
                    if (cookingTime > 0) {
                        _cookingTime.postValue(cookingTime.minus(1))
                    } else {
                        _cookingTime.postValue(0)
                    }
                }
            }
        }
    }

    /**
     * Calculates the total nutrients from the list of nutrition information.
     */
    fun calculateNutrients() {
        var calorie = 0.0
        var protein = 0.0
        var carbs = 0.0
        if (!_nutritionList.value.isNullOrEmpty()) {
            for (e in _nutritionList.value!!) {
                calorie += e.calories
                protein += e.proteinG
                carbs += e.carbohydratesTotalG
            }
        }
        calculatedNutrients.clear()
        calculatedNutrients.add(calorie.roundToInt())
        calculatedNutrients.add(protein.roundToInt())
        calculatedNutrients.add(carbs.roundToInt())

    }
}