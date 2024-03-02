package com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.TagConstant
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentRecipeAddBinding
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.utils.ui.RecipeMappers
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for adding a new recipe.
 */
@AndroidEntryPoint
class RecipeAddFragment : Fragment() {
    private var _binding: FragmentRecipeAddBinding? = null
    private val viewModel: RecipeAddUpdateViewModel by viewModels()
    private lateinit var adapter: FoodCategoriesAdapter

    // Lazily initialize binding using the _binding property
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        viewModel.getCategoryList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_add, container, false)
        adapter = FoodCategoriesAdapter(this@RecipeAddFragment,
            object : FoodCategoriesAdapter.OnCheckItemClickListener {
                override fun checkItemClick(category: FoodCategory) {
                    val index =
                        viewModel.checkedCategoriesList.indexOfFirst { it.idFoodCategory == category.idFoodCategory } // Assuming FoodCategory has an "id" field
                    if (index != -1) {
                        viewModel.checkedCategoriesList[index] = category
                    }
                }

            })

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            addRecipeVM = viewModel
            rvFoodCategory.also {
                it.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                it.adapter = adapter
            }
            ibIncreServeCount.setOnClickListener {
                viewModel.increaseCount(TagConstant.TAG_SERVE_COUNT)
            }
            ibDecreServeCount.setOnClickListener {
                viewModel.decreaseCount(TagConstant.TAG_SERVE_COUNT)
            }
            ibIncreCookingTime.setOnClickListener {
                viewModel.increaseCount(TagConstant.TAG_COOKING_TIME)
            }
            ibDecreCookingTime.setOnClickListener {
                viewModel.decreaseCount(TagConstant.TAG_COOKING_TIME)
            }
            btnSave.setOnClickListener {
                Log.e("DILAN", "onCreateView: ${viewModel.checkedCategoriesList}")
            }

            // Set HTML formatted text to the TextView
            mtvInfoIngredient.text = HtmlCompat.fromHtml(getString(R.string.ingredients_html), HtmlCompat.FROM_HTML_MODE_LEGACY)
            mtvInfoInstruction.text = HtmlCompat.fromHtml(getString(R.string.instructions_html), HtmlCompat.FROM_HTML_MODE_LEGACY)

            ivInfoIngredient.setOnClickListener {
                if(mtvInfoIngredient.visibility == VISIBLE){
                    mtvInfoIngredient.visibility = GONE
                } else {
                    mtvInfoIngredient.visibility = VISIBLE
                }
            }

            ivInfoInstruction.setOnClickListener {
                if(mtvInfoInstruction.visibility == VISIBLE){
                    mtvInfoInstruction.visibility = GONE
                } else {
                    mtvInfoInstruction.visibility = VISIBLE
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            // Observe nutrition data changes
            nutrition.observe(viewLifecycleOwner) {
                // TODO: Attach calory constraints with saving recipe item
                Log.e("CHECK", "onViewCreated: ${it[0].name}")
            }
            categoryList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    for (i in it) {
                        i.categoryImageId = RecipeMappers.categoryImageMap[i.idFoodCategory]!!
                    }
                    viewModel.checkedCategoriesList = it.toMutableList()
                    adapter.submitList(viewModel.checkedCategoriesList)
                } else {
                    // TODO: Show no category items loaded yet
                }
            }
            saveRecipeSuccess.observe(viewLifecycleOwner) {
                if (it != null) {
                    Toast.makeText(
                        requireContext(),
                        "Recipe Save Successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to save the recipe!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            serveCount.observe(viewLifecycleOwner) {
                binding.metServingCount.text =
                    Editable.Factory.getInstance().newEditable(it.toString())
            }
            cookingTime.observe(viewLifecycleOwner) {
                binding.metCookingTime.text =
                    Editable.Factory.getInstance().newEditable(it.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Nullify the binding to avoid memory leaks
        _binding = null
    }
}