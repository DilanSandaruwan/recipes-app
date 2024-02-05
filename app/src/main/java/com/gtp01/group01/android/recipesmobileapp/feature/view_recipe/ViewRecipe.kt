package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gtp01.group01.android.recipesmobileapp.R

class ViewRecipe : Fragment() {

    companion object {
        fun newInstance() = ViewRecipe()
    }

    private lateinit var viewModel: ViewRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_recipe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewRecipeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}