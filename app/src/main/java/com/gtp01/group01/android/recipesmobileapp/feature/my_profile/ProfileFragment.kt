package com.gtp01.group01.android.recipesmobileapp.feature.my_profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.AuthProviders
import com.gtp01.group01.android.recipesmobileapp.constant.AuthUtils
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantRequestCode.MY_REQUEST_CODE
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentProfileBinding
import com.gtp01.group01.android.recipesmobileapp.feature.main.MainActivity

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        // Access the list of providers
        val providers = AuthProviders.providers
        //Event
        binding.btnLogout.setOnClickListener {
            // Signout
            AuthUI.getInstance().signOut(requireContext())
                .addOnCompleteListener {
                    // Redirect to the sign-in options after successful sign-out
                    AuthUtils.showSignInOptions(requireActivity(), MY_REQUEST_CODE, providers)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
        }

        if (currentUser != null) {
            val username = currentUser.displayName
            // Now 'username' contains the display name of the currently logged-in user

            // You can set the username to your TextView or wherever you want to display it
            binding.usernameTextView.text = username
        } else {
            // Handle the case where no user is logged in
            binding.usernameTextView.text = "Guest User" // Placeholder text for the username

            // Example: Show a Toast message
            Toast.makeText(
                requireContext(),
                "Please log in to view your profile",
                Toast.LENGTH_SHORT
            ).show()

            // Example: Redirect to the login screen (assuming you have a LoginActivity)

        }


    }
}


