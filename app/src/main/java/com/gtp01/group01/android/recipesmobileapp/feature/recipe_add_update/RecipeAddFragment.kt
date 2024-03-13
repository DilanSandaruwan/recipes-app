package com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.TagConstant
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentRecipeAddBinding
import com.gtp01.group01.android.recipesmobileapp.databinding.LayoutDuplicateIngredientItemBinding
import com.gtp01.group01.android.recipesmobileapp.feature.main.MainActivity
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategoryApp
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import com.gtp01.group01.android.recipesmobileapp.shared.utils.ui.RecipeMappers
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

/**
 * Fragment for adding a new recipe.
 * This fragment allows users to add a new recipe, including details like recipe name, ingredients, instructions, serving count, cooking time, and image.
 * Users can also select categories for the recipe and save it.
 */
@AndroidEntryPoint
class RecipeAddFragment : Fragment() {
    // View binding for the fragment
    private var _binding: FragmentRecipeAddBinding? = null

    // Lazily initialize binding using the _binding property
    private val binding get() = _binding!!

    // ViewModel for handling recipe addition/update logic
    private val viewModel: RecipeAddUpdateViewModel by viewModels()

    // Adapter for displaying food categories
    private lateinit var adapter: FoodCategoriesAdapter

    // Reference to the MainActivity
    private lateinit var activity: MainActivity


    /**
     * Opens the gallery to select an image for the recipe.
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    // Activity result launcher for gallery intent
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val galleryUri = result.data?.data
                try {
                    galleryUri?.let {
                        binding.ivChooseItemImg.setImageURI(it)
                        binding.ivChooseItemImg.visibility = VISIBLE
                        // Convert the selected image to a byte array
                        viewModel.imageBytes = convertImageUriToByteArray(it)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    /**
     * Converts the selected image URI to a byte array.
     */
    private fun convertImageUriToByteArray(imageUri: Uri): ByteArray? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            // Resize the image before converting to a byte array
            val options = BitmapFactory.Options()
            options.inSampleSize = 2 // Adjust this value as needed to control image size
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)

            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

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
     * Sets up UI components and event listeners.
     */
    private fun setupUI(): View {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            addRecipeVM = viewModel
            rvFoodCategory.also {
                it.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                it.adapter = adapter
            }
            // Set HTML formatted text to the TextView
            mtvInfoIngredient.text = HtmlCompat.fromHtml(
                getString(R.string.ingredients_html),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            mtvInfoInstruction.text = HtmlCompat.fromHtml(
                getString(R.string.instructions_html),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            eventListeners()

        }
        return binding.root
    }

    /**
     * Sets up event listeners for UI components.
     */
    private fun eventListeners() {
        binding.apply {
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
            btnAddIngredient.setOnClickListener {
                addIngredientField()
            }
            btnAddInstructions.setOnClickListener {
                addInstructionsField()
            }
            btnChooseItemImg.setOnClickListener {
                openGallery()
            }
            btnSave.setOnClickListener {
                validateRecipeDetails()
            }
            ivInfoIngredient.setOnClickListener {
                if (mtvInfoIngredient.visibility == VISIBLE) {
                    mtvInfoIngredient.visibility = GONE
                } else {
                    mtvInfoIngredient.visibility = VISIBLE
                }
            }

            ivInfoInstruction.setOnClickListener {
                if (mtvInfoInstruction.visibility == VISIBLE) {
                    mtvInfoInstruction.visibility = GONE
                } else {
                    mtvInfoInstruction.visibility = VISIBLE
                }
            }
            lytPopupIncluded.ivPopupClose.setOnClickListener {
                binding.lytPopupIncluded.lytPopupScreen.visibility = GONE
                activity.binding.navView.visibility = VISIBLE
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity() as MainActivity
        initObservers()
    }

    /**
     * Initializes observers for LiveData objects in the ViewModel.
     */
    private fun initObservers() {
        viewModel.apply {
            // Observe nutrition data changes
            nutritionList.observe(viewLifecycleOwner) {
                viewModel.calculateNutrients()
                if (viewModel.calculatedNutrients.sum() == 0) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.warn_please_add_ingredients_in_correct_format),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        binding.lytDynamicIngredients.focusable
                    }
                } else {
                    callToSaveRecipe()

                }
            }
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
            saveRecipeSuccess.observe(viewLifecycleOwner) {
                if (it != null) {
                    showPopup(
                        0,
                        getString(R.string.success),
                        message = getString(R.string.recipe_saved_successfully)
                    )
                    clearScreenForANewRecipe()
                } else {
                    showPopup(
                        1,
                        getString(R.string.error),
                        getString(R.string.error_occured_while_saving_the_recipe)
                    )
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

    /**
     * Displays a popup message.
     */
    private fun showPopup(type: Int, title: String, message: String) {
        var icon: Int = R.drawable.ic_info_popup
        when (type) {
            0 -> {
                icon = R.drawable.ico_selected_item
            }

            1 -> {
                icon = R.drawable.ic_error_popup
            }

            2 -> {
                icon = R.drawable.ic_info_popup
            }
        }
        binding.lytPopupIncluded.ivPopupIcon.setImageResource(icon)
        binding.lytPopupIncluded.mtvPopupTitle.text = title
        binding.lytPopupIncluded.mtvPopupDescription.text = message
        binding.lytPopupIncluded.lytPopupScreen.visibility = VISIBLE
        activity.binding.navView.visibility = GONE
    }

    /**
     * Adds a new ingredient field to the UI.
     */
    private fun addIngredientField() {
        val subBinding = LayoutDuplicateIngredientItemBinding.inflate(layoutInflater)
        subBinding.btnDeleteIngredient.setOnClickListener {
            binding.lytDynamicIngredients.removeView(subBinding.root)
        }

        if (binding.lytDynamicIngredients.childCount > 0) {
            for (i in binding.lytDynamicIngredients.children) {
                val metInsertIngredient =
                    i.findViewById<TextInputEditText>(R.id.metDynamicInserter)
                if (metInsertIngredient.text.toString()
                        .isEmpty() || metInsertIngredient.text.toString().isBlank()
                ) {
                    return
                }
            }
        }
        val view = subBinding.root
        binding.lytDynamicIngredients.addView(view)
    }

    /**
     * Adds a new instruction field to the UI.
     */
    private fun addInstructionsField() {
        val subBinding = LayoutDuplicateIngredientItemBinding.inflate(layoutInflater)
        subBinding.btnDeleteIngredient.setOnClickListener {
            binding.lytDynamicInstructions.removeView(subBinding.root)
        }

        if (binding.lytDynamicInstructions.childCount > 0) {
            for (i in binding.lytDynamicInstructions.children) {
                val metInsertInstructions =
                    i.findViewById<TextInputEditText>(R.id.metDynamicInserter)
                if (metInsertInstructions.text.toString()
                        .isEmpty() || metInsertInstructions.text.toString().isBlank()
                ) {
                    return
                }
            }
        }
        val view = subBinding.root
        binding.lytDynamicInstructions.addView(view)
    }

    /**
     * Validates recipe details before saving.
     */
    private fun validateRecipeDetails() {
        val errorString = if (binding.etRecipeName.editText?.text.toString().isBlank()) {
            getString(R.string.warn_please_add_a_recipe_name)
        } else if (binding.metServingCount.text.toString()
                .isBlank() || binding.metServingCount.text.toString() == getString(R.string.default_zero)
        ) {
            getString(R.string.warn_please_add_a_serving_count)
        } else if (binding.metCookingTime.text.toString()
                .isBlank() || binding.metCookingTime.text.toString() == getString(R.string.default_zero)
        ) {
            getString(R.string.warn_please_add_a_cooking_time)
        } else if (viewModel.editableCategoriesList.size < 1) {
            getString(R.string.warn_please_choose_atleast_a_category)
        } else if ((viewModel.imageBytes?.size ?: 0) == 0) {
            getString(R.string.warn_please_add_an_image)
        } else if (binding.lytDynamicIngredients.childCount < 1) {
            getString(R.string.warn_please_add_atleast_an_ingredient)
        } else if (binding.lytDynamicInstructions.childCount < 1) {
            getString(R.string.warn_please_add_atleast_an_instruction)
        } else {
            null
        }

        if (errorString == null) {
            callNutrientsApi()
        } else {
            Snackbar.make(requireContext(), binding.btnSave, errorString, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    /**
     * Calls the API to fetch nutrition information based on ingredients.
     */
    private fun callNutrientsApi() {
        if (binding.lytDynamicIngredients.childCount > 0) {
            for (i in 0 until binding.lytDynamicIngredients.childCount) {
                val childView = binding.lytDynamicIngredients.getChildAt(i)
                val metInsertIngredients =
                    childView.findViewById<TextInputEditText>(R.id.metDynamicInserter)
                if (!(metInsertIngredients.text.toString()
                        .isEmpty() || metInsertIngredients.text.toString().isBlank()
                            )
                ) {
                    viewModel.ingredientsList.add(metInsertIngredients.text.toString())
                }
            }
        }
        val ingredients = viewModel.ingredientsList.joinToString(" and ")
        viewModel.getNutritionsVm(ingredients)
    }

    /**
     * Initiates the process of saving the recipe.
     */
    private fun callToSaveRecipe() {
        val user = User(idUser = 10)
        val foodCategories = ArrayList<FoodCategory>().toMutableList()
        for (i in viewModel.editableCategoriesList) {
            if (i.isSelected) {
                foodCategories.add(FoodCategory(i.idFoodCategory, i.categoryName))
            }
        }

        val ingredients = viewModel.ingredientsList.joinToString("\n")

        if (binding.lytDynamicInstructions.childCount > 0) {
            for (i in 0 until binding.lytDynamicInstructions.childCount) {
                val childView = binding.lytDynamicInstructions.getChildAt(i)
                val metInsertInstructions =
                    childView.findViewById<TextInputEditText>(R.id.metDynamicInserter)
                if (!(metInsertInstructions.text.toString()
                        .isEmpty() || metInsertInstructions.text.toString().isBlank()
                            )
                ) {
                    viewModel.instructionsList.add(metInsertInstructions.text.toString())
                }
            }
        }
        val instructions = viewModel.instructionsList.joinToString("\n")

        val recipe = Recipe(
            owner = user,
            recipeName = binding.etRecipeName.editText?.text.toString(),
            ingredients = ingredients,
            instruction = instructions,
            preparationTime = binding.metCookingTime.text.toString().toInt(),
            calorie = viewModel.calculatedNutrients[0],
            protein = viewModel.calculatedNutrients[1],
            carbs = viewModel.calculatedNutrients[2],
            serving = binding.metServingCount.text.toString().toInt(),
            photo = viewModel.imageBytes,
            categories = foodCategories,
            isActive = 1,
            likeCount = 0,
            hasLike = false,
            hasFavorite = false,
        )
        viewModel.saveRecipe(10, recipe)
    }

    /**
     * Clears the screen for adding a new recipe.
     */
    private fun clearScreenForANewRecipe() {
        // clear recipe name edit text field
        binding.etRecipeName.editText?.text?.clear()

        // clear checked category list
        viewModel.editableCategoriesList.forEach { i -> i.isSelected = false }
        adapter.submitList(viewModel.editableCategoriesList)

        // clear calculated category list
        viewModel.calculatedNutrients.clear()

        // clear ingredients list
        viewModel.ingredientsList.clear()

        // clear instructions list
        viewModel.instructionsList.clear()

        // Remove all child views from lytDynamicIngredients
        binding.lytDynamicIngredients.removeAllViews()

        // Remove all child views from lytDynamicIngredients
        binding.lytDynamicInstructions.removeAllViews()

        // default counts
        viewModel.setDefaultCount(TagConstant.TAG_SERVE_COUNT)
        viewModel.setDefaultCount(TagConstant.TAG_COOKING_TIME)

        // clear image bytes array
        viewModel.imageBytes = null

        // hide the image
        binding.ivChooseItemImg.visibility = GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        // Nullify the binding to avoid memory leaks
        _binding = null
    }
}