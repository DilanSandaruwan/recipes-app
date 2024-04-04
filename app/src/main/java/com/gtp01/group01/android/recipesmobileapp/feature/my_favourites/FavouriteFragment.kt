package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentFavouriteBinding
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentHomeBinding
import com.gtp01.group01.android.recipesmobileapp.feature.main.MainActivity
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.adapters.RecipesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var recipesAdapter: RecipesAdapter
    lateinit var binding: FragmentFavouriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteBinding.bind(view)



        favouriteViewModel = (activity as MainActivity).favouriteViewModel
        setupFavoriteRecycler()
        recipesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("recipes", it)
            }

            findNavController().navigate(R.id.action_favouriteFragment_to_homeFragment, bundle)
        }





        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val recipes = recipesAdapter.differ.currentList[position]
                favouriteViewModel.deleteRecipe(recipes)
                Snackbar.make(view, "Remove from Favorites", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        favouriteViewModel.addToFavorites(recipes)
                    }
                    show()
                }

            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }

        favouriteViewModel.getFavoriteRecipes().observe(viewLifecycleOwner, Observer { recipes ->
            recipesAdapter.differ.submitList(recipes)
        })




    }


        private fun setupFavoriteRecycler() {
            recipesAdapter = RecipesAdapter()
            binding.recyclerFavourites.apply {
                adapter = recipesAdapter
                layoutManager = LinearLayoutManager(activity)
            }

        }


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_favourite, container, false)
        }

    }

