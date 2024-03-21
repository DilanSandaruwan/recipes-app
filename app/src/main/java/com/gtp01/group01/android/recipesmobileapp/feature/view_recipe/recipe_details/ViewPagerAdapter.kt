package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter for managing the ViewPager2 fragments in the recipe details screen.
 * This adapter manages two fragments: InstructionFragment and IngredientsFragment.
 * @param fragment The parent fragment to which this adapter belongs.
 */
class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    /**
     * Returns the total number of fragments managed by this adapter.
     * @return The total number of fragments.
     */

    override fun getItemCount(): Int = 2
    /**
     * Creates and returns the fragment corresponding to the specified position.
     * @param position The position of the fragment.
     * @return The fragment instance.
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {

            0 -> InstructionFragment() // Create and return InstructionFragment for position 0.
            else -> IngredientsFragment()// Create and return IngredientsFragment for any other position.
        }
    }
}