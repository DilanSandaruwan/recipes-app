package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gtp01.group01.android.recipesmobileapp.R

class IngredientsAdapter(private val context: Context, private val formattedIngredients: Any?
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ingredients_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (formattedIngredients is List<*>) {
            val ingredient = formattedIngredients[position] as? String
            ingredient?.let {
                holder.bind(it)
            }
        }
    }

    override fun getItemCount(): Int {
        if (formattedIngredients is List<*>) {
            return formattedIngredients.size
        }
        return 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textIngredientName: TextView = itemView.findViewById(R.id.tvIngriedents)

        fun bind(ingredient: String) {
            textIngredientName.text = ingredient
        }
    }
}