package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gtp01.group01.android.recipesmobileapp.databinding.ItemRecipeBinding
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

class FavoritesAdapter(
    private val favoriteRecipes: List<Recipe>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipe: Recipe)
    }

    inner class ViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
           // binding.recipe = recipe
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.onItemClick(recipe) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favoriteRecipes[position])
    }

    override fun getItemCount(): Int = favoriteRecipes.size
}
