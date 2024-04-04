package com.gtp01.group01.android.recipesmobileapp.feature.my_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.RecipeManagementRepository
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategoryApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for editing profile information.
 * Retrieves and manages data related to food categories.
 *
 * @param recipeManagementRepository The repository for managing recipe-related data.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val recipeManagementRepository: RecipeManagementRepository
) : ViewModel() {
    // LiveData for category list
    private val _categoryList = MutableLiveData<List<FoodCategory>>()
    val categoryList: LiveData<List<FoodCategory>> = _categoryList

    // List of editable food categories
    var editableCategoriesList: MutableList<FoodCategoryApp> = mutableListOf()


    /**
     * Fetches the list of food categories asynchronously from the repository.
     * The fetched data is posted to [_categoryList].
     */
    fun getCategoryList() {
        viewModelScope.launch {
            val response = recipeManagementRepository.getCategoryList()
            _categoryList.postValue(response ?: emptyList())
        }
    }

}