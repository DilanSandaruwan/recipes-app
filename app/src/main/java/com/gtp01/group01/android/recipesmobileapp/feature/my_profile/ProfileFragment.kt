package com.gtp01.group01.android.recipesmobileapp.feature.my_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.AuthProviders
import com.gtp01.group01.android.recipesmobileapp.constant.AuthProviders.providers
import com.gtp01.group01.android.recipesmobileapp.constant.AuthUtils
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantRequestCode.MY_REQUEST_CODE
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentProfileBinding
import com.gtp01.group01.android.recipesmobileapp.shared.sources.Local.LocalDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Fragment to display and manage user profile information.
 *
 * This fragment is responsible for showing user details retrieved from Firebase Authentication
 * and initiating the process of saving user information to the backend through the ViewModel.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var localDataSource: LocalDataSource // Inject LocalDataSource

    // Current authenticated user obtained from Firebase Authentication
    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding

        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Find the TextView
        val tvEditProfile = requireView().findViewById<TextView>(R.id.tvEditProfile)

        // Set OnClickListener for tvNotification
        tvEditProfile.setOnClickListener {
            // Navigate back to the previous fragment
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Access the list of providers
        val providers = AuthProviders.providers

        viewUserDetails()
        binding.tvNotification
        // Sign-out from Firebase Authentication


        binding.ivLogout.setOnClickListener {
            logout()
        }

        binding.tvLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout() {

        // Remove user ID from SharedPreferences
        localDataSource.deleteUserId()
        // Signout
        AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
            // Redirect to the sign-in options after successful sign-out
            AuthUtils.showSignInOptions(requireActivity(), MY_REQUEST_CODE, providers)
        }.addOnFailureListener { e ->
            // Log the error for debugging purposes
            // Show a Toast on sign-out failure
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            Log.e("ProfileFragment", "Sign-out failed: ${e.message}")
        }
    }

    /**
     * Display user details on the UI.
     * If the user is authenticated, show their display name, email, and UID.
     * If not authenticated, display a placeholder message for a guest user.
     */
    private fun viewUserDetails() {
        if (currentUser != null) {
            val userId = currentUser.uid
            val userEmail = currentUser.email
            val userDisplayName = currentUser.displayName
            // Now 'username' contains the display name of the currently logged-in user
            binding.usernameTextView.text = userDisplayName
            binding.tvUserEmail.text = userEmail
            Log.d("ProfileFragment", "Username: $userDisplayName,$userEmail,$userId")
        } else {
            // Handle the case where no user is logged in
            binding.usernameTextView.text = getString(R.string.guest_user) // Placeholder text for the username
            binding.tvUserEmail.text=getString(R.string.guest_user)
            // Example:
            Toast.makeText(
                requireContext(), "Please log in to view your profile", Toast.LENGTH_SHORT
            ).show()
            // Example: Redirect to the login screen (assuming you have a LoginActivity)
        }
    }
}