package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentFavouriteBinding
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment(), FavoritesAdapter.OnItemClickListener {
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var viewModel: FavoritesViewModel

    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =  ViewModelProvider(this).get(FavoritesViewModel::class.java)
        adapter = FavoritesAdapter(emptyList(), this)
        binding.recyclerFavourites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavourites.adapter = adapter

        viewModel.favoriteRecipes.observe(viewLifecycleOwner, Observer { favorites ->
            adapter.favoriteRecipes = favorites
            adapter.notifyDataSetChanged()
        })

        // Fetch favorite recipes
        viewModel.fetchFavoriteRecipes(userId = 10)
    }

    override fun onItemClick(recipe: Recipe) {
        // Handle item click
        }
}