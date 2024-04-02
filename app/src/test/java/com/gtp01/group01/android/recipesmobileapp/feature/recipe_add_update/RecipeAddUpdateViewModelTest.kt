package com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gtp01.group01.android.recipesmobileapp.data.RecipesData.getIngredientsDataForNutrientsApi
import com.gtp01.group01.android.recipesmobileapp.data.RecipesData.getNutritionModelListData
import com.gtp01.group01.android.recipesmobileapp.data.RecipesData.getOriginalCategoriesListData
import com.gtp01.group01.android.recipesmobileapp.data.RecipesData.getSavedRecipeData
import com.gtp01.group01.android.recipesmobileapp.data.RecipesData.getSavingRecipeData
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.RecipeManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Unit tests for [RecipeAddUpdateViewModel] class.
 *
 * This class contains test cases for validating the behavior of the RecipeAddUpdateViewModel.
 */
@ExperimentalCoroutinesApi
class RecipeAddUpdateViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ObsoleteCoroutinesApi
    private val testScope = TestCoroutineScope(testDispatcher)

    @Mock
    lateinit var recipeManagementRepository: RecipeManagementRepository

    private lateinit var viewModel: RecipeAddUpdateViewModel

    /**
     * Sets up the test environment before each test case.
     */
    @OptIn(ObsoleteCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = RecipeAddUpdateViewModel(recipeManagementRepository)
    }

    /**
     * Cleans up the test environment after each test case.
     */
    @OptIn(ObsoleteCoroutinesApi::class)
    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    /**
     * Tests the [RecipeAddUpdateViewModel.getNutritionsVm] function.
     * It verifies that the correct nutrition list is retrieved and set in the view model.
     */
    @Test
    fun `test getNutritionsVm`() = runBlocking {
        val ingredients = getIngredientsDataForNutrientsApi()
        val nutritionList = getNutritionModelListData()

        `when`(recipeManagementRepository.getNutritionsRepo(ingredients)).thenReturn(nutritionList)

        viewModel.getNutritionsVm(ingredients)

        verify(recipeManagementRepository).getNutritionsRepo(ingredients)
        Thread.sleep(15000) // Use enough time for asynchronous operation to complete
        assert(viewModel.nutritionList.value == nutritionList)
    }

    /**
     * Tests the [RecipeAddUpdateViewModel.getCategoryList] function.
     * It verifies that the correct category list is retrieved and set in the view model.
     */
    @Test
    fun `test getCategoryList`() = runBlocking {
        val categoryList = getOriginalCategoriesListData()

        `when`(recipeManagementRepository.getCategoryList()).thenReturn(categoryList)

        viewModel.getCategoryList()

        verify(recipeManagementRepository).getCategoryList()
        assert(viewModel.categoryList.value == categoryList)
    }

    /**
     * Tests the [RecipeAddUpdateViewModel.saveRecipe] function.
     * It verifies that the recipe is saved successfully and the appropriate result is set in the view model.
     */
    @Test
    fun `test saveRecipe`() = runBlocking {
        val idLoggedUser = 1
        val recipe = getSavingRecipeData()
        val savedRecipe = getSavedRecipeData()

        `when`(recipeManagementRepository.saveNewRecipe(idLoggedUser, recipe)).thenReturn(
            savedRecipe
        )

        viewModel.saveRecipe(idLoggedUser, recipe)

        verify(recipeManagementRepository).saveNewRecipe(idLoggedUser, recipe)
        assert(viewModel.saveRecipeSuccess.value == savedRecipe)
    }

}