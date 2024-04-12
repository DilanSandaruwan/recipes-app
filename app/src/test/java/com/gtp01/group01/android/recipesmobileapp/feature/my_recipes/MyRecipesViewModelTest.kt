package com.gtp01.group01.android.recipesmobileapp.feature.my_recipes

import com.gtp01.group01.android.recipesmobileapp.data.RecipeListTestData
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.RecipeManagementRepository
import com.gtp01.group01.android.recipesmobileapp.shared.common.Logger
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.nullable
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for [MyRecipesViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
@ObsoleteCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MyRecipesViewModelTest {

    private lateinit var viewModel: MyRecipesViewModel

    @Mock
    private lateinit var recipeManagementRepository: RecipeManagementRepository

    @Mock
    private lateinit var logger: Logger // Mocked logger

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = MyRecipesViewModel(recipeManagementRepository, logger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests [MyRecipesViewModel.getMyRecipes] when it should emit loading state.
     */
    @Test
    fun `getMyRecipes should emit loading state`(): Unit = runTest {
        // Given
        val userId = 1
        val flow = flow<Result<List<Recipe>>> { emit(Result.Loading) }
        `when`(recipeManagementRepository.getMyRecipes(userId)).thenReturn(flow)

        // When
        viewModel.getMyRecipes(userId)

        // Then
        assertEquals(Result.Loading, viewModel.myRecipesList.value)
    }

    /**
     * Tests [MyRecipesViewModel.getMyRecipes] when it should emit success state.
     */
    @Test
    fun `getMyRecipes should emit success state`() = runTest {
        // Given
        val userId = 1
        val recipes = RecipeListTestData.getRecipeList()
        `when`(recipeManagementRepository.getMyRecipes(userId)).thenReturn(
            flow {
                emit(Result.Success(recipes))
            })

        // When
        viewModel.getMyRecipes(userId)

        // Then
        val result: Result<List<Recipe>> = viewModel.myRecipesList.first()
        assertTrue(result is Result.Success)
        assertTrue(((result as Result.Success).result).containsAll(recipes))
    }

    /**
     * Tests [MyRecipesViewModel.getMyRecipes] when it should emit failure state.
     */
    @Test
    fun `getMyRecipes should emit failure state`() = runTest {
        // Given
        val userId = 1
        val testException = Exception("Exception")
        `when`(recipeManagementRepository.getMyRecipes(userId)).thenReturn(
            flow { emit(Result.Failure(testException.message.toString())) }
        )

        // When
        viewModel.getMyRecipes(userId)

        // Then
        val result: Result<List<Recipe>> = viewModel.myRecipesList.first()
        assertTrue(result is Result.Failure)
        assertTrue(((result as Result.Failure).error).contentEquals("Exception"))
    }

    /**
     * Tests [MyRecipesViewModel.decodeImageToBitmap] when it should handle decoding error.
     */
    @Test
    fun `decodeImageToBitmap should handle decoding error`() {
        // Given
        val base64Image = "invalid_base64_encoded_string"

        // When
        val result = viewModel.decodeImageToBitmap(base64Image)

        // Then
        assertNull(result)
        verify(logger).error(anyString(), anyString(), nullable(Throwable::class.java))
    }

}
