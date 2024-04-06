package com.gtp01.group01.android.recipesmobileapp.feature.my_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.LayoutCategoryItemBinding
import com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update.FoodCategoriesAdapter
import com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update.RecipeAddFragment
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategoryApp

/**
 * Adapter for displaying food categories in a RecyclerView.
 *
 * @param context The context associated with the adapter.
 * @param onCheckItemClickListener The listener for item click events.
 */
class FoodCategoryViewAdapter(
    val context: EditProfileFragment,
    private val onCheckItemClickListener: OnCheckItemClickListener
) : ListAdapter<FoodCategoryApp, FoodCategoryViewAdapter.ViewHolder>(diffUtil) {

    /**
     * ViewHolder for the FoodCategoryViewAdapter.
     */
    class ViewHolder(private val binding: LayoutCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data to the ViewHolder.
         */
        fun bind(category: FoodCategoryApp, onCheckItemClickListener: OnCheckItemClickListener) {
            with(binding) {
                // Bind category name and image to the layout
                mtvCategoryName.text = category.categoryName
                ivCategory.setImageResource(category.categoryImageId)

                // Set item selected or not
                rimSelectedOrNot.setBackgroundResource(
                    if (category.isSelected) R.drawable.ico_selected_item
                    else R.drawable.ico_unselected_item
                )

                // Handle item click events
                rimSelectedOrNot.setOnClickListener {
                    // Toggle category selection and update UI
                    category.isSelected = !category.isSelected
                    rimSelectedOrNot.setBackgroundResource(
                        if (category.isSelected) R.drawable.ico_selected_item
                        else R.drawable.ico_unselected_item
                    )
                    // Notify listener about item click
                    onCheckItemClickListener.checkItemClick(category)
                }
            }
        }
    }

    /**
     * Creates a ViewHolder for the adapter.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    /**
     * Binds data to the ViewHolder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onCheckItemClickListener)
    }

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

    companion object {
        /**
         * DiffUtil.ItemCallback for comparing list items.
         */
        private val diffUtil = object : DiffUtil.ItemCallback<FoodCategoryApp>() {
            override fun areItemsTheSame(
                oldItem: FoodCategoryApp,
                newItem: FoodCategoryApp
            ): Boolean {
                return oldItem.idFoodCategory == newItem.idFoodCategory
            }

            override fun areContentsTheSame(
                oldItem: FoodCategoryApp,
                newItem: FoodCategoryApp
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}