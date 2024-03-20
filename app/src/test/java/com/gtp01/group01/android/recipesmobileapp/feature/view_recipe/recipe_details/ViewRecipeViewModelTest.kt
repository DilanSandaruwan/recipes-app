package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.ViewRecipeRepository
import com.gtp01.group01.android.recipesmobileapp.shared.common.ResultState
import com.gtp01.group01.android.recipesmobileapp.shared.model.AuthUser
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ViewRecipeViewModelTest{

    // Coroutine dispatcher for testing
    private val testDispatcher = TestCoroutineDispatcher()

    // Rule to make LiveData work synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mocked repository
    @Mock
    private lateinit var viewRecipeRepository: ViewRecipeRepository

    // ViewModel to be tested
    private lateinit var viewModel: ViewRecipeViewModel

    /**
     * Setup method to initialize necessary components before each test.
     */
    @Before
    fun setup() {

        // Initialize Mockito annotations
        initMocks(this)
        // Set main dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)
        // Initialize ViewModel with mocked repositories
        viewModel = ViewRecipeViewModel(viewRecipeRepository)

    }
    /**
     * Test case to verify that fetchRecipeDetail updates recipeDetails LiveData with Success state.
     */
    @Test
    fun `fetchRecipeDetail updates recipeDetails LiveData with Success state`() = runBlockingTest {
        // Mock successful result from repository
        val userId = 1
        val recipeId = 1
        val mockRecipe = Recipe(
            recipeId,
            AuthUser(1,"p@gmail.com","sadun"),
            "Mock Recipe",
            "Mock Ingredients",
            "Mock Instruction",
            30,
            200,
            20,
            30,
            4,
            "Mock Photo",
            listOf(FoodCategory(1,"psa")),
            1,
            10,
            true,
            true,
            null // Initially bitmap is null
        )
        `when`(viewRecipeRepository.getRecipeDetail(anyInt(), anyInt())).thenReturn(mockRecipe)

        // Invoke the method under test
        viewModel.fetchRecipeDetail(userId, recipeId)

        // Ensure that recipeDetails LiveData is updated with Success state and correct data
        val resultState = viewModel.recipeDetails.value
        assertEquals(ResultState.Success::class.java, resultState?.javaClass)

        // Check if the bitmap property is null
        val recipe = (resultState as? ResultState.Success<Recipe>)?.result
        if (recipe != null) {
            assertEquals(null, recipe.bitmap)
        }
    }

    @Test
    fun `fetchRecipeDetail updates recipeDetails LiveData with Failure state`() = runBlockingTest {
        // Mock failure from repository
        val userId = 1
        val recipeId = 1
        val errorMessage = "Mock Error Message"
        `when`(viewRecipeRepository.getRecipeDetail(anyInt(), anyInt())).thenThrow(RuntimeException(errorMessage))

        // Invoke the method under test
        viewModel.fetchRecipeDetail(userId, recipeId)

        // Ensure that recipeDetails LiveData is updated with Failure state and correct error message
        val resultState = viewModel.recipeDetails.value
        assertEquals(ResultState.Failure::class.java, resultState?.javaClass)
        assertEquals(errorMessage, (resultState as? ResultState.Failure)?.error)
    }

}