package com.gtp01.group01.android.recipesmobileapp.feature.my_profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentEditProfileBinding
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentRecipeAddBinding
import com.gtp01.group01.android.recipesmobileapp.feature.main.MainActivity
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategoryApp
import com.gtp01.group01.android.recipesmobileapp.shared.utils.ui.RecipeMappers
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for editing the user's profile.
 */
@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null

    // ViewModel
    private lateinit var viewModel: EditProfileViewModel


    // Adapter for RecyclerView
    private lateinit var adapter: FoodCategoryViewAdapter
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        viewModel.getCategoryList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

        // Initialize RecyclerView adapter
        adapter = FoodCategoryViewAdapter(
            this,
            object : FoodCategoryViewAdapter.OnCheckItemClickListener {
                override fun checkItemClick(category: FoodCategoryApp) {
                    val index =
                        viewModel.editableCategoriesList.indexOfFirst { it.idFoodCategory == category.idFoodCategory } // Assuming FoodCategory has an "id" field
                    if (index != -1) {
                        viewModel.editableCategoriesList[index] = category
                    }
                }
            })
        return setupUI()


    }

    /**
     * Sets up the UI components.
     */
    private fun setupUI(): View {
        binding.apply {

            rvFoodCategory.also {
                it.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                it.adapter = adapter
            }


        }
        return binding.root
    }

    /**
     * Called when the fragment's activity is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
    }

    /**
     * Initializes observers for LiveData.
     */
    private fun initObservers() {

        viewModel.apply {
            categoryList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    viewModel.editableCategoriesList.clear()
                    for (i in it) {
                        val foodCategoryApp = FoodCategoryApp(
                            i.idFoodCategory,
                            i.categoryName,
                            RecipeMappers.categoryImageMap[i.idFoodCategory]!!
                        )
                        viewModel.editableCategoriesList.add(foodCategoryApp)
                    }
                    adapter.submitList(viewModel.editableCategoriesList)
                } else {
                    viewModel.editableCategoriesList = emptyList<FoodCategoryApp>().toMutableList()
                }
            }
        }
    }

    /**
     * Called when the fragment is being destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}