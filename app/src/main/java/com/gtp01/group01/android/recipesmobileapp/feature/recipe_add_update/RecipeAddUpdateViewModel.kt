package com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.constant.TagConstant
import com.gtp01.group01.android.recipesmobileapp.repository.RecipeManagementRepository
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.models.NutritionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    var checkedCategoriesList: MutableList<FoodCategory> = mutableListOf()

    private val _serveCount = MutableLiveData(0)
    val serveCount: LiveData<Int> = _serveCount

    private val _cookingTime = MutableLiveData(0)
    val cookingTime: LiveData<Int> = _cookingTime

    private val _nutritionList = MutableLiveData<List<NutritionModel>>()
    val nutrition: LiveData<List<NutritionModel>> = _nutritionList

    private val _categoryList = MutableLiveData<List<FoodCategory>>()
    val categoryList: LiveData<List<FoodCategory>> = _categoryList

    private val _saveRecipeSuccess = MutableLiveData<Recipe>()
    val saveRecipeSuccess: LiveData<Recipe> = _saveRecipeSuccess

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

    fun getCategoryList() {
        viewModelScope.launch {
            val response = recipeManagementRepository.getCategoryList()
            _categoryList.postValue(response ?: emptyList())
        }
    }

    fun saveRecipe(idLoggedUser: Int, recipe: Recipe) {
        viewModelScope.launch {
            val response = recipeManagementRepository.saveNewRecipe(idLoggedUser, recipe)
            _saveRecipeSuccess.postValue(recipe)
        }
    }

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
}