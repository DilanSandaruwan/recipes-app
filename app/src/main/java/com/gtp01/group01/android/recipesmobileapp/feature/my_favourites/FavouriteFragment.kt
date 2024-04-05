package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentFavouriteBinding
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment(), FavoritesAdapter.OnItemClickListener {
    private lateinit var binding: FragmentFavouriteBinding
    private val viewModel: FavoritesViewModel by viewModels()

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

        adapter = FavoritesAdapter(emptyList(), this)
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter

        viewModel.favoriteRecipes.observe(viewLifecycleOwner, Observer { favorites ->
            adapter.favoriteRecipes = favorites
            adapter.notifyDataSetChanged()
        })

        // Fetch favorite recipes
        viewModel.fetchFavoriteRecipes()
    }

    override fun onItemClick(recipe: Recipe) {
        // Handle item click
    }
}
