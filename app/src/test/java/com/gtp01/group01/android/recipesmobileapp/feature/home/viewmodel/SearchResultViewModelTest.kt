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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


/**
 * Unit tests for the SearchResultViewModel class.
 */
@RunWith(MockitoJUnitRunner::class)
class SearchResultViewModelTest {
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
     * Instance of HomeViewModel to be tested.
     */
    private lateinit var viewModel: SearchResultViewModel

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
        viewModel = SearchResultViewModel(
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
     * Test case to verify that searchResultRecipeListState is updated correctly when filtering recipes by name.
     */
    @Test
    fun `filterRecipesByName updates searchResultRecipeListState correctly`() = runTest {
        // Mock data
        val loggedUserId = 1
        val recipeName = "Spaghetti"
        val recipeList = RecipeListTestData.getRecipeList()


        // Mock repository response
        Mockito.`when`(
            recipeManagementRepository.filterRecipesByName(
                loggedUserId,
                recipeName.trim()
            )
        )
            .thenReturn(MutableStateFlow(Result.Success(recipeList)))


        // Call the method under test
        viewModel.filterRecipesByName(loggedUserId, recipeName)

        // Verify that searchResultRecipeListState is updated correctly
        assert(viewModel.searchResultRecipeListState.value is Result.Success)
        assert((viewModel.searchResultRecipeListState.value as Result.Success).result == recipeList)
    }

    /**
     * Test case to verify that searchResultRecipeListState is updated correctly when filtering recipes by category.
     */
    @Test
    fun `filterRecipesByCategory updates searchResultRecipeListState correctly`() = runTest {
        // Given a logged-in user ID and a category ID
        val userId = 1
        val categoryId = 2
        val recipeList = RecipeListTestData.getRecipeList()


        // Mock repository response
        Mockito.`when`(recipeManagementRepository.filterRecipesByCategory(userId, categoryId))
            .thenReturn(MutableStateFlow(Result.Success(recipeList)))

        // When filterRecipesByCategory is called with the logged-in user ID and category ID
        viewModel.filterRecipesByCategory(userId, categoryId)

        // Then searchResultRecipeListState should be updated with the fetched recipe list
        assertEquals(Result.Success(recipeList), viewModel.searchResultRecipeListState.value)
    }

    /**
     * Test case to verify that likeRecipe executes onSuccess callback correctly on success.
     */
    @Test
    fun `likeRecipe executes onSuccess callback correctly on success`() = runTest {
        // Given a logged-in user ID and a recipe ID
        val userId = 1
        val recipeId = 1

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.likeRecipe(userId, recipeId))
            .thenReturn(MutableStateFlow(Result.Success(true)))

        // Initialize variables to track the callbacks
        var onSuccessCalled = false
        var onFailureCalled = false
        var onLoadingCalled = false

        // When likeRecipe is called with the logged-in user ID and recipe ID
        viewModel.likeRecipe(
            userId,
            recipeId,
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true },
            onLoading = { onLoadingCalled = true }
        )

        // Ensure that onSuccess callback was invoked
        assertTrue(onSuccessCalled)

        // Ensure that onFailure and onLoading callbacks were not invoked
        assertFalse(onFailureCalled)
        assertFalse(onLoadingCalled)
    }

    /**
     * Test case to verify that likeRecipe executes onFailure callback correctly on failure.
     */
    @Test
    fun `likeRecipe executes onFailure callback correctly on failure`() = runTest {
        // Given a logged-in user ID and a recipe ID
        val userId = 1
        val recipeId = 1
        val errorMessage = "Failed to like recipe"

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.likeRecipe(userId, recipeId))
            .thenReturn(MutableStateFlow(Result.Failure(errorMessage)))

        // Initialize variables to track the callbacks
        var onSuccessCalled = false
        var onFailureCalled = false
        var onLoadingCalled = false

        // When likeRecipe is called with the logged-in user ID and recipe ID
        viewModel.likeRecipe(
            userId,
            recipeId,
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true },
            onLoading = { onLoadingCalled = true }
        )

        // Ensure that onFailure callback was invoked
        assertTrue(onFailureCalled)

        // Ensure that onSuccess and onLoading callbacks were not invoked
        assertFalse(onSuccessCalled)
        assertFalse(onLoadingCalled)
    }

    /**
     * Test case to verify that removeLikeRecipe executes onSuccess callback correctly on success.
     */
    @Test
    fun `removeLikeRecipe executes onSuccess callback correctly on success`() = runTest {
        // Given a logged-in user ID and a recipe ID
        val userId = 1
        val recipeId = 1

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.removeLikeRecipe(userId, recipeId))
            .thenReturn(MutableStateFlow(Result.Success(true)))

        // Initialize variables to track the callbacks
        var onSuccessCalled = false
        var onFailureCalled = false
        var onLoadingCalled = false

        // When removeLikeRecipe is called with the logged-in user ID and recipe ID
        viewModel.removeLikeRecipe(
            userId,
            recipeId,
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true },
            onLoading = { onLoadingCalled = true }
        )

        // Ensure that onSuccess callback was invoked
        assertTrue(onSuccessCalled)

        // Ensure that onFailure and onLoading callbacks were not invoked
        assertFalse(onFailureCalled)
        assertFalse(onLoadingCalled)
    }

    /**
     * Test case to verify that removeLikeRecipe executes onFailure callback correctly on failure.
     */
    @Test
    fun `removeLikeRecipe executes onFailure callback correctly on failure`() = runTest {
        // Given a logged-in user ID and a recipe ID
        val userId = 1
        val recipeId = 1
        val errorMessage = "Failed to like recipe"

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.removeLikeRecipe(userId, recipeId))
            .thenReturn(MutableStateFlow(Result.Failure(errorMessage)))

        // Initialize variables to track the callbacks
        var onSuccessCalled = false
        var onFailureCalled = false
        var onLoadingCalled = false

        // When removeLikeRecipe is called with the logged-in user ID and recipe ID
        viewModel.removeLikeRecipe(
            userId,
            recipeId,
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true },
            onLoading = { onLoadingCalled = true }
        )

        // Ensure that onFailure callback was invoked
        assertTrue(onFailureCalled)

        // Ensure that onSuccess and onLoading callbacks were not invoked
        assertFalse(onSuccessCalled)
        assertFalse(onLoadingCalled)
    }

    /**
     * Test case to verify that addFavoriteRecipe executes onSuccess callback correctly on success.
     */
    @Test
    fun `addFavoriteRecipe executes onSuccess callback correctly on success`() = runTest {
        // Given a logged-in user ID and a recipe ID
        val userId = 1
        val recipeId = 1

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.addFavoriteRecipe(userId, recipeId))
            .thenReturn(MutableStateFlow(Result.Success(true)))

        // Initialize variables to track the callbacks
        var onSuccessCalled = false
        var onFailureCalled = false
        var onLoadingCalled = false

        // When addFavoriteRecipe is called with the logged-in user ID and recipe ID
        viewModel.addFavoriteRecipe(
            userId,
            recipeId,
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true },
            onLoading = { onLoadingCalled = true }
        )

        // Ensure that onSuccess callback was invoked
        assertTrue(onSuccessCalled)

        // Ensure that onFailure and onLoading callbacks were not invoked
        assertFalse(onFailureCalled)
        assertFalse(onLoadingCalled)
    }

    /**
     * Test case to verify that addFavoriteRecipe executes onFailure callback correctly on failure.
     */
    @Test
    fun `addFavoriteRecipe executes onFailure callback correctly on failure`() = runTest {
        // Given a logged-in user ID and a recipe ID
        val userId = 1
        val recipeId = 1
        val errorMessage = "Failed to like recipe"

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.addFavoriteRecipe(userId, recipeId))
            .thenReturn(MutableStateFlow(Result.Failure(errorMessage)))

        // Initialize variables to track the callbacks
        var onSuccessCalled = false
        var onFailureCalled = false
        var onLoadingCalled = false

        // When addFavoriteRecipe is called with the logged-in user ID and recipe ID
        viewModel.addFavoriteRecipe(
            userId,
            recipeId,
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true },
            onLoading = { onLoadingCalled = true }
        )

        // Ensure that onFailure callback was invoked
        assertTrue(onFailureCalled)

        // Ensure that onSuccess and onLoading callbacks were not invoked
        assertFalse(onSuccessCalled)
        assertFalse(onLoadingCalled)
    }

    /**
     * Test case to verify that removeFavoriteRecipe executes onSuccess callback correctly on success.
     */
    @Test
    fun `removeFavoriteRecipe executes onSuccess callback correctly on success`() = runTest {
        // Given a logged-in user ID and a recipe ID
        val userId = 1
        val recipeId = 1

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.removeFavoriteRecipe(userId, recipeId))
            .thenReturn(MutableStateFlow(Result.Success(true)))

        // Initialize variables to track the callbacks
        var onSuccessCalled = false
        var onFailureCalled = false
        var onLoadingCalled = false

        // When removeFavoriteRecipe is called with the logged-in user ID and recipe ID
        viewModel.removeFavoriteRecipe(
            userId,
            recipeId,
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true },
            onLoading = { onLoadingCalled = true }
        )

        // Ensure that onSuccess callback was invoked
        assertTrue(onSuccessCalled)

        // Ensure that onFailure and onLoading callbacks were not invoked
        assertFalse(onFailureCalled)
        assertFalse(onLoadingCalled)
    }

    /**
     * Test case to verify that removeFavoriteRecipe executes onFailure callback correctly on failure.
     */
    @Test
    fun `removeFavoriteRecipe executes onFailure callback correctly on failure`() = runTest {
        // Given a logged-in user ID and a recipe ID
        val userId = 1
        val recipeId = 1
        val errorMessage = "Failed to like recipe"

        // Mock repository response
        Mockito.`when`(recipeManagementRepository.removeFavoriteRecipe(userId, recipeId))
            .thenReturn(MutableStateFlow(Result.Failure(errorMessage)))

        // Initialize variables to track the callbacks
        var onSuccessCalled = false
        var onFailureCalled = false
        var onLoadingCalled = false

        // When removeFavoriteRecipe is called with the logged-in user ID and recipe ID
        viewModel.removeFavoriteRecipe(
            userId,
            recipeId,
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true },
            onLoading = { onLoadingCalled = true }
        )

        // Ensure that onFailure callback was invoked
        assertTrue(onFailureCalled)

        // Ensure that onSuccess and onLoading callbacks were not invoked
        assertFalse(onSuccessCalled)
        assertFalse(onLoadingCalled)
    }
}