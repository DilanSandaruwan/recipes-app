package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
class FavoritesAdapter(

    var favoriteRecipes: List<Recipe>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    /**
     * Interface definition for a callback to be invoked when a favorite recipe item is clicked.
     */
    interface OnItemClickListener {
        /**
         * Called when a favorite recipe item is clicked.
         * @param recipe The clicked recipe.
         */
        fun onItemClick(recipe: Recipe)
    }

    /**
     * ViewHolder for displaying each favorite recipe item.
     * @param itemView The layout view for the item.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val recipeDescription: TextView = itemView.findViewById(R.id.recipeDescription)
        val articleDateTime: TextView = itemView.findViewById(R.id.articleDateTime)

        /**
         * Binds the recipe data to the view elements.
         * @param recipe The recipe to bind.
         */
        fun bind(recipe: Recipe) {
            recipeTitle.text = recipe.recipeName
            recipeDescription.text = recipe.instruction
            articleDateTime.text = "Preparation time: ${recipe.preparationTime} mins"

            // Load the bitmap into ImageView if it's not null
            recipe.bitmap?.let { bitmap ->
                recipeImage.setImageBitmap(bitmap)
            }

            // Set click listener
            itemView.setOnClickListener { listener.onItemClick(recipe) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_recipes_favourite, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favoriteRecipes[position])
    }

    override fun getItemCount(): Int = favoriteRecipes.size


    }