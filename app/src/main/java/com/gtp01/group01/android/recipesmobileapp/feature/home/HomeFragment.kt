package com.gtp01.group01.android.recipesmobileapp.feature.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentHomeBinding
import com.gtp01.group01.android.recipesmobileapp.feature.main.MainActivity
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.FavouriteViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var favouriteViewModel: FavouriteViewModel

    lateinit var binding: FragmentHomeBinding

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)







    }







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {

    }
}