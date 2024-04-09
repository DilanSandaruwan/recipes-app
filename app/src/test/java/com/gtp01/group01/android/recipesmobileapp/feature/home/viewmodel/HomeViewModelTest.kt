package com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gtp01.group01.android.recipesmobileapp.data.RecipeListTestData
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.RecipeManagementRepository
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.common.viewmodel.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Test class for HomeViewModel.
 *
 * This class contains unit tests for various functionalities of the HomeViewModel class.
 */
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
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

    /**
     * Mock instance of ConnectivityManager.
     */
    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    /**
     * Mock instance of RecipeManagementRepository.
     */
    @Mock
    private lateinit var recipeManagementRepository: RecipeManagementRepository

    /**
     * Mock instance of SharedViewModel.
     */
    @Mock
    private lateinit var sharedViewModel: SharedViewModel

    /**
     * Mock instance of NetworkRequest.
     */
    @Mock
    private lateinit var networkRequest: NetworkRequest

    /**
     * Mock instance of ConnectivityManager.NetworkCallback.
     */
    @Mock
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    /**
     * Mock instance of Network.
     */
    @Mock
    private lateinit var network: Network

    /**
     * Argument captor for ConnectivityManager.NetworkCallback.
     */
    @Captor
    private lateinit var networkCallbackCaptor: ArgumentCaptor<ConnectivityManager.NetworkCallback>

    /**
     * Instance of HomeViewModel to be tested.
     */
    private lateinit var viewModel: HomeViewModel

    /**
     * Setup method to initialize the objects before each test case.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(
            connectivityManager.registerNetworkCallback(
                Mockito.any(NetworkRequest::class.java),
                Mockito.any(ConnectivityManager.NetworkCallback::class.java)
            )
        ).thenAnswer {
            val args = it.arguments
            if (args.size == 2 && args[0] is NetworkRequest && args[1] is ConnectivityManager.NetworkCallback) {
                networkCallback = args[1] as ConnectivityManager.NetworkCallback
            }
        }
        viewModel = HomeViewModel(
            connectivityManager = connectivityManager,
            recipeManagementRepository = recipeManagementRepository,
            sharedViewModel = sharedViewModel,
            networkRequest = networkRequest
        )
    }

    /**
     * Test case to verify that networkAvailable LiveData updates correctly on network changes.
     */
    @Test
    fun `networkAvailable updates on network changes`() = runTest {
        viewModel.networkAvailable.observeForever { }

        // Simulate network available
        networkCallback.onAvailable(network)
        assertTrue(viewModel.networkAvailable.value!!)

        // Simulate network lost
        networkCallback.onLost(network)
        assertFalse(viewModel.networkAvailable.value!!)
    }

    /**
     * Test case to verify that timeBasedRecipeListState updates correctly when filtering recipes by duration.
     */
    @Test
    fun `filterRecipesByDuration updates timeBasedRecipeListState correctly`() = runTest {
        val userId = 1
        val maxDuration = 30
        val recipeList = RecipeListTestData.getRecipeList()

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.filterRecipesByDuration(userId, maxDuration))
            .thenReturn(MutableStateFlow(Result.Success(recipeList)))

        // Call the method under test
        viewModel.filterRecipesByDuration(userId, maxDuration)

        // Verify that timeBasedRecipeListState is updated correctly
        assert(viewModel.timeBasedRecipeListState.value is Result.Success)
        assert((viewModel.timeBasedRecipeListState.value as Result.Success).result == recipeList)
    }

    /**
     * Test case to verify that filterRecipesByDuration returns failure state when an error occurs.
     */
    @Test
    fun `filterRecipesByDuration returns failure state when error`() = runTest {
        val userId = 1
        val maxDuration = 30

        val testException = "Something went wrong"

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.filterRecipesByDuration(userId, maxDuration))
            .thenReturn(MutableStateFlow(Result.Failure(testException)))

        // Call the method under test
        viewModel.filterRecipesByDuration(userId, maxDuration)

        // Verify that timeBasedRecipeListState is updated correctly
        assert(viewModel.timeBasedRecipeListState.value is Result.Failure)
        assert((viewModel.timeBasedRecipeListState.value as Result.Failure).error.contentEquals("Something went wrong"))
    }

    /**
     * Test case to verify that filterRecipesByCalorie updates calorieBasedRecipeListState correctly.
     */
    @Test
    fun `filterRecipesByCalorie updates calorieBasedRecipeListState correctly`() = runTest {
        val userId = 1
        val maxCalorie = 300
        val recipeList = RecipeListTestData.getRecipeList()

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.filterRecipesByCalorie(userId, maxCalorie))
            .thenReturn(MutableStateFlow(Result.Success(recipeList)))

        // Call the method under test
        viewModel.filterRecipesByCalorie(userId, maxCalorie)

        // Verify that calorieBasedRecipeListState is updated correctly
        assert(viewModel.calorieBasedRecipeListState.value is Result.Success)
        assert((viewModel.calorieBasedRecipeListState.value as Result.Success).result == recipeList)
    }

    /**
     * Test case to verify that filterRecipesByCalorie returns failure state when an error occurs.
     */
    @Test
    fun `filterRecipesByCalorie returns failure state when error`() = runTest {
        val userId = 1
        val maxCalorie = 30

        val testException = "Something went wrong"

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.filterRecipesByCalorie(userId, maxCalorie))
            .thenReturn(MutableStateFlow(Result.Failure(testException)))

        // Call the method under test
        viewModel.filterRecipesByCalorie(userId, maxCalorie)

        // Verify that calorieBasedRecipeListState is updated correctly
        assert(viewModel.calorieBasedRecipeListState.value is Result.Failure)
        assert((viewModel.calorieBasedRecipeListState.value as Result.Failure).error.contentEquals("Something went wrong"))
    }

    /**
     * Test case to verify that updateSearchKeyword updates searchKeyword correctly.
     */
    @Test
    fun `updateSearchKeyword updates searchKeyword correctly`() {
        val keyword = "TestKeyword"

        // Call the method under test
        viewModel.updateSearchKeyword(keyword)

        // Verify that searchKeyword is updated correctly
        assert(viewModel.searchKeyword == keyword)
    }

    /**
     * Test case to verify that isValidKeyword returns true for a valid keyword.
     */
    @Test
    fun `isValidKeyword returns true for valid keyword`() {
        val validKeyword = "Valid123"

        // Call the method under test
        val isValid = viewModel.isValidKeyword(validKeyword)

        // Verify that isValidKeyword returns true for a valid keyword
        assert(isValid)
    }

    /**
     * Test case to verify that isValidKeyword returns false for an invalid keyword.
     */
    @Test
    fun `isValidKeyword returns false for invalid keyword`() {
        val invalidKeyword = "Invalid@Keyword"

        // Call the method under test
        val isValid = viewModel.isValidKeyword(invalidKeyword)

        // Verify that isValidKeyword returns false for an invalid keyword
        assert(!isValid)
    }
}