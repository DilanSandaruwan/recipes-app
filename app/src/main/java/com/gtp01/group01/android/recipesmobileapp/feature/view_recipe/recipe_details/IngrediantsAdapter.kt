package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gtp01.group01.android.recipesmobileapp.R
/**
 * Adapter for displaying a list of ingredients in a RecyclerView.
 *
 * @property context The context of the calling activity or fragment.
 * @property ingredients The array of ingredients to display.
 */
class IngredientsAdapter(private val context: Context, private var ingredients: Array<String>) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    /**
     * Inflates the item view layout and creates a ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ingredients_list_item, parent, false)
        return ViewHolder(view)
    }
    /**
     * Binds data to the ViewHolder at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }
    /**
     * Returns the total number of items in the data set.
     */
    override fun getItemCount(): Int = ingredients.size
    /**
     * Updates the list of ingredients with new data.
     *
     * @param newIngredients The new array of ingredients to display.
     */
    fun updateIngredients(newIngredients: Array<String>) {
        ingredients = newIngredients

        notifyDataSetChanged()
    }
    /**
     * ViewHolder class for holding references to the views for each item.
     *
     * @param itemView The inflated view for an item in the RecyclerView.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textIngredientName: TextView = itemView.findViewById(R.id.tvIngriedents)
        /**
         * Binds the ingredient data to the views.
         *
         * @param ingredient The ingredient to display.
         */
        fun bind(ingredient: String) {
            textIngredientName.text = ingredient
        }
    }
}
