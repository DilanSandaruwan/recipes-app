package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.models.Recipes

class RecipesAdapter: RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    inner class RecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    lateinit var recipesImage: ImageView

    private val differCallback = object : DiffUtil.ItemCallback<Recipes>(){
        override fun areItemsTheSame(oldItem: Recipes, newItem: Recipes): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Recipes, newItem: Recipes): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        return RecipesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recipes,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener: ((Recipes) ->Unit)? = null

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val recipes = differ.currentList[position]

        recipesImage = holder.itemView.findViewById(R.id.recipeImage)



        holder.itemView.apply {
            Glide.with(this).load(recipes.urlto)


            }
        }
    }


}