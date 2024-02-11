package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.ViewRecipe


class ViewPagerAdapter (fragment:Fragment):FragmentStateAdapter(fragment){


    override fun getItemCount(): Int= 2

    override fun createFragment(position: Int): Fragment {
        return when(position){

            0->InstructionFragment()
            else -> IngredientsFragment()
        }
    }
}