package com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.LayoutCategoryItemBinding
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategoryApp

/**
 * Adapter for displaying food categories in the recipe addition fragment.
 * This adapter binds the data of FoodCategoryApp to the RecyclerView.
 *
 * @param context The context of the RecipeAddFragment.
 * @param onCheckItemClickListener Listener for item click events.
 */
class FoodCategoriesAdapter(
    val context: Any,
    private val onCheckItemClickListener: OnCheckItemClickListener
) : ListAdapter<FoodCategoryApp, FoodCategoriesAdapter.ViewHolder>(diff_util) {

    /**
     * ViewHolder for the FoodCategoriesAdapter.
     *
     * @param binding LayoutCategoryItemBinding for each item view.
     */
    class ViewHolder(val binding: LayoutCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Interface for item click events.
     */
    interface OnCheckItemClickListener {
        /**
         * Called when a category item is clicked.
         *
         * @param category The FoodCategoryApp object representing the clicked category.
         */
        fun checkItemClick(category: FoodCategoryApp)
    }

    /**
     * Companion object for defining DiffUtil.ItemCallback for comparing list items.
     */
    companion object {
        /**
         * DiffUtil.ItemCallback for comparing list items.
         */
        val diff_util = object : DiffUtil.ItemCallback<FoodCategoryApp>() {
            /**
             * Checks if two items are the same.
             */
            override fun areItemsTheSame(
                oldItem: FoodCategoryApp,
                newItem: FoodCategoryApp
            ): Boolean {
                return oldItem.idFoodCategory == newItem.idFoodCategory
            }

            /**
             * Checks if the contents of two items are the same.
             */
            override fun areContentsTheSame(
                oldItem: FoodCategoryApp,
                newItem: FoodCategoryApp
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    /**
     * Creates a ViewHolder for the adapter.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds data to the ViewHolder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                // Bind category name and image to the layout
                binding.mtvCategoryName.text = this.categoryName
                binding.ivCategory.setImageResource(this.categoryImageId)

                // Set item selected or not
                if (this.isSelected)
                    binding.rimSelectedOrNot.setBackgroundResource(R.drawable.ico_selected_item)
                else
                    binding.rimSelectedOrNot.setBackgroundResource(R.drawable.ico_unselected_item)

                // Handle item click events
                binding.rimSelectedOrNot.setOnClickListener {
                    // Toggle category selection and update UI
                    if (this.isSelected) {
                        this.isSelected = !isSelected
                        binding.rimSelectedOrNot.setBackgroundResource(R.drawable.ico_unselected_item)
                    } else {
                        this.isSelected = !isSelected
                        binding.rimSelectedOrNot.setBackgroundResource(R.drawable.ico_selected_item)
                    }
                    // Notify listener about item click
                    onCheckItemClickListener.checkItemClick(this)
                }
            }
        }
    }

}
