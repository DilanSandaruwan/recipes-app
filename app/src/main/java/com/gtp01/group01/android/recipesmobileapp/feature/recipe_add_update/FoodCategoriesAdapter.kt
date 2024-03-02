package com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.LayoutCategoryItemBinding
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory

class FoodCategoriesAdapter(
    val context: RecipeAddFragment,
    private val onCheckItemClickListener: FoodCategoriesAdapter.OnCheckItemClickListener
) : ListAdapter<FoodCategory, FoodCategoriesAdapter.ViewHolder>(FoodCategoriesAdapter.diff_util) {
    class ViewHolder(val binding: LayoutCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    interface OnCheckItemClickListener {
        fun checkItemClick(category: FoodCategory)
    }


    companion object {

        val diff_util = object : DiffUtil.ItemCallback<FoodCategory>() {

            override fun areItemsTheSame(oldItem: FoodCategory, newItem: FoodCategory): Boolean {
                return oldItem.idFoodCategory == newItem.idFoodCategory
            }

            override fun areContentsTheSame(
                oldItem: FoodCategory,
                newItem: FoodCategory
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                binding.mtvCategoryName.text = this.categoryName
                binding.lytMatCardCategoryItem.setBackgroundResource(this.categoryImageId)
                binding.rimSelectedOrNot.setOnClickListener{
                    if(this.isSelected){
                        this.isSelected = !isSelected
                        binding.rimSelectedOrNot.setBackgroundResource(R.drawable.ico_unselected_item)
                    } else {
                        this.isSelected = !isSelected
                        binding.rimSelectedOrNot.setBackgroundResource(R.drawable.ico_selected_item)
                    }
                    onCheckItemClickListener.checkItemClick(this)
                }
            }
        }
    }

}
