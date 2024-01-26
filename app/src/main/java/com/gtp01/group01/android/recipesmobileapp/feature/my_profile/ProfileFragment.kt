package com.gtp01.group01.android.recipesmobileapp.feature.my_profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentProfileBinding
import com.gtp01.group01.android.recipesmobileapp.feature.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ProfileFragment : Fragment() {
    private lateinit var viewModel : ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // Customize your GoogleSignInOptions as needed
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        // Access your views using the binding object
        binding.logout.setOnClickListener {
            // Replace mGoogleSignInClient with your actual GoogleSignInClient instance
            // (initialize it in your class or retrieve it from your dependencies)
            // For example, you might have something like:
            // val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

            // mGoogleSignInClient.signOut()...
            viewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            Toast.makeText(requireContext(), "Logging Out", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            requireActivity().finish()

    }   }

    companion object {
        // Your companion object if needed
    }
}