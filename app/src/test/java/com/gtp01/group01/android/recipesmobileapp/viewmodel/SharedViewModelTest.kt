package com.gtp01.group01.android.recipesmobileapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gtp01.group01.android.recipesmobileapp.constant.UserDefaultConstant.GUEST_CALORIE_PREFERENCE
import com.gtp01.group01.android.recipesmobileapp.constant.UserDefaultConstant.GUEST_DURATION_PREFERENCE
import com.gtp01.group01.android.recipesmobileapp.constant.UserDefaultConstant.GUEST_USER_ID
import com.gtp01.group01.android.recipesmobileapp.shared.common.viewmodel.SharedViewModel
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategoryApp
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for the SharedViewModel class.
 */
@RunWith(MockitoJUnitRunner::class)
class SharedViewModelTest {
    /**
     * Rule to allow LiveData to be tested synchronously using JUnit.
     */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * TestDispatcher to run coroutines in tests.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @ObsoleteCoroutinesApi
    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    lateinit var user: User

    private lateinit var viewModel: SharedViewModel

    /**
     * Setup method executed before each test to initialize required objects.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = SharedViewModel()
    }

    /**
     * Test case to verify that setCurrentUser updates savedUser with default values for a guest user.
     */
    @Test
    fun `setCurrentUser updates savedUser with default values for guest user`() = runTest {
        // Given a guest user
        Mockito.`when`(user.idUser).thenReturn(GUEST_USER_ID)

        // When setCurrentUser is called with the guest user
        viewModel.setCurrentUser(user)

        // Then savedUser should contain default values for guest user
        val savedUser = viewModel.savedUser.value
        assertEquals(GUEST_USER_ID, savedUser.idUser)
        assertEquals(GUEST_DURATION_PREFERENCE, savedUser.preferDuration)
        assertEquals(GUEST_CALORIE_PREFERENCE, savedUser.preferCalorie)
    }

    /**
     * Test case to verify that setCurrentUser updates savedUser with provided user data for a logged-in user.
     */
    @Test
    fun `setCurrentUser updates savedUser with provided user data`() = runTest {
        // Given a logged-in user with custom preferences
        val userId = 123
        val email = "test@example.com"
        val fullName = "John Doe"
        val preferCategories = listOf(
            FoodCategoryApp(
                idFoodCategory = 1,
                categoryName = "bread",
                categoryImageId = 1
            ),
        )
        val preferDuration = 45
        val preferCalorie = 500
        Mockito.`when`(user.idUser).thenReturn(userId)
        Mockito.`when`(user.email).thenReturn(email)
        Mockito.`when`(user.fullName).thenReturn(fullName)
        Mockito.`when`(user.preferCategories).thenReturn(preferCategories)
        Mockito.`when`(user.preferDuration).thenReturn(preferDuration)
        Mockito.`when`(user.preferCalorie).thenReturn(preferCalorie)

        // When setCurrentUser is called with the logged-in user
        viewModel.setCurrentUser(user)

        // Then savedUser should contain the provided user data
        val savedUser = viewModel.savedUser.value
        assertEquals(userId, savedUser.idUser)
        assertEquals(email, savedUser.email)
        assertEquals(fullName, savedUser.fullName)
        assertEquals(preferCategories, savedUser.preferCategories)
        assertEquals(preferDuration, savedUser.preferDuration)
        assertEquals(preferCalorie, savedUser.preferCalorie)
    }
}