package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import com.gtp01.group01.android.recipesmobileapp.shared.sources.AuthApiService
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

/**
 * Unit tests for the ViewRecipeRepository class.
 */
class ViewRecipeRepositoryTest {
    // Mocked AuthApiService
    private lateinit var authApiService: AuthApiService

    // Repository to be tested
    private lateinit var repository: ViewRecipeRepository

    /**
     * Setup method to initialize necessary components before each test.
     */
    @Before
    fun setup() {
        authApiService = mock(AuthApiService::class.java)
        repository = ViewRecipeRepository(authApiService)
    }

    /**
     * Test case to verify that getRecipeDetail returns recipe when API call is successful.
     */
    @Test
    fun `getRecipeDetail returns recipe when API call is successful`() = runBlocking {
        // Mock successful API response
        val recipeId = 1
        val userId = 1
        val mockRecipe = Recipe(
            recipeId, User(1, "p@gmail.com", "sadun"),
            "Mock Recipe",
            "Mock Ingredients",
            "Mock Instruction",
            30,
            200,
            20,
            30,
            4,
            "Mock Photo",
            listOf(FoodCategory(1, "psa")),
            1,
            10,
            true,
            true,
            null // Initially bitmap is null //
        )
        `when`(authApiService.getRecipeDetail(anyInt(), anyInt())).thenReturn(
            Response.success(
                mockRecipe
            )
        )

        // Invoke the method under test
        val result = repository.getRecipeDetail(userId, recipeId)

        // Verify that the result is as expected
        assertEquals(mockRecipe, result)
    }

    /**
     * Test case to verify that getRecipeDetail returns null when API call is unsuccessful.
     */
    @Test
    fun `getRecipeDetail returns null when API call is unsuccessful`() = runBlocking {
        // Mock unsuccessful API response
        val recipeId = 1
        val userId = 1
        `when`(authApiService.getRecipeDetail(anyInt(), anyInt())).thenReturn(
            Response.error(
                404,
                ResponseBody.create(null, "")
            )
        )

        // Invoke the method under test
        val result = repository.getRecipeDetail(userId, recipeId)

        // Verify that the result is null
        assertNull(result)
    }

    /**
     * Test case to verify that getRecipeDetail returns null when exception is thrown.
     */
    @Test
    fun `getRecipeDetail returns null when exception is thrown`() = runBlocking {
        // Mock exception being thrown by the API call
        val recipeId = 1
        val userId = 1
        `when`(
            authApiService.getRecipeDetail(
                anyInt(),
                anyInt()
            )
        ).thenThrow(RuntimeException("Mock Exception"))

        // Invoke the method under test
        val result = repository.getRecipeDetail(userId, recipeId)

        // Verify that the result is null
        assertNull(result)
    }


}