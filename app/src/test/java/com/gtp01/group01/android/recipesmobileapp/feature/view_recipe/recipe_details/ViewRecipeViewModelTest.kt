package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.ViewRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
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
}