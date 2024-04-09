package com.gtp01.group01.android.recipesmobileapp.feature.recipe_delete_update

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
import android.view.View.INVISIBLE
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.TagConstant
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentRecipeUpdateBinding
import com.gtp01.group01.android.recipesmobileapp.databinding.LayoutDuplicateIngredientItemBinding
import com.gtp01.group01.android.recipesmobileapp.feature.main.MainActivity
import com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update.FoodCategoriesAdapter
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategoryApp
import com.gtp01.group01.android.recipesmobileapp.shared.sources.Local.LocalDataSource
import com.gtp01.group01.android.recipesmobileapp.shared.utils.RecipeMappers
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/**
 * Fragment for updating a recipe.
 * Handles UI interactions for updating recipe details.
 */
@AndroidEntryPoint
class RecipeUpdateFragment : Fragment() {

    @Inject
    lateinit var localDataSource: LocalDataSource // Inject LocalDataSource

    // View binding for the fragment
    private var _binding: FragmentRecipeUpdateBinding? = null

    // Lazily initialize binding using the _binding property
    private val binding get() = _binding!!

    // ViewModel for handling recipe addition/update logic
    private val viewModel: RecipeUpdateViewModel by viewModels()

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
                        binding.lytImportedRecipeUpdate.ivChooseItemImg.setImageURI(it)
                        binding.lytImportedRecipeUpdate.ivChooseItemImg.visibility = View.VISIBLE
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
     * @param imageUri The URI of the selected image.
     * @return The byte array representing the image.
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_update, container, false)
        adapter = FoodCategoriesAdapter(this@RecipeUpdateFragment,
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
     * @return The root view of the fragment.
     */
    private fun setupUI(): View {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            updateRecipeVM = viewModel
            lytImportedRecipeUpdate.appBarTitle.text = getString(R.string.update_my_recipe_title)
            lytImportedRecipeUpdate.rvFoodCategory.also {
                it.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                it.adapter = adapter
            }
            // Set HTML formatted text to the TextView
            lytImportedRecipeUpdate.mtvInfoIngredient.text = HtmlCompat.fromHtml(
                getString(R.string.ingredients_html),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            lytImportedRecipeUpdate.mtvInfoInstruction.text = HtmlCompat.fromHtml(
                getString(R.string.instructions_html),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            lytImportedRecipeUpdate.btnUpdate.visibility = VISIBLE
            lytImportedRecipeUpdate.btnSave.visibility = GONE
            lytImportedRecipeUpdate.btnDelete.visibility = GONE

            eventListeners()

        }
        return binding.root
    }

    /**
     * Sets up event listeners for UI components.
     */
    private fun eventListeners() {
        binding.lytImportedRecipeUpdate.apply {
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
            btnUpdate.setOnClickListener {
                if (viewModel.recipeFromArgs != null) {
                    validateRecipeDetails()
                } else {
                    activity.showPopup(
                        2,
                        getString(R.string.info_nothing),
                        getString(R.string.nothing_to_update),
                    )
                }
            }
            ivInfoIngredient.setOnClickListener {
                if (mtvInfoIngredient.visibility == View.VISIBLE) {
                    mtvInfoIngredient.visibility = View.GONE
                } else {
                    mtvInfoIngredient.visibility = View.VISIBLE
                }
            }

            ivInfoInstruction.setOnClickListener {
                if (mtvInfoInstruction.visibility == View.VISIBLE) {
                    mtvInfoInstruction.visibility = View.GONE
                } else {
                    mtvInfoInstruction.visibility = View.VISIBLE
                }
            }

            btnNavigateBack.setOnClickListener {
                navigateBackToMyRecipe()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity() as MainActivity
        activity.setBottomNavVisibility(false)
        val args: RecipeUpdateFragmentArgs by navArgs()
        initObservers()
        viewModel.getOneRecipe(
            localDataSource.getUserId(),
            args.recipeId
        )
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
                        binding.lytImportedRecipeUpdate.lytDynamicIngredients.focusable
                    }
                } else {
                    val uiCalorie = viewModel.calculatedNutrients[0]
                    val uiProtein = viewModel.calculatedNutrients[1]
                    val uiCarbs = viewModel.calculatedNutrients[2]

                    viewModel.recipeFromArgs?.calorie = uiCalorie
                    viewModel.recipeFromArgs?.protein = uiProtein
                    viewModel.recipeFromArgs?.carbs = uiCarbs

                    callToUpdateRecipe()
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
                        if (viewModel.recipeFromArgs?.categories?.any { category -> i.idFoodCategory == category.idFoodCategory } == true) {
                            foodCategoryApp.isSelected = true
                        }
                        viewModel.editableCategoriesList.add(foodCategoryApp)
                    }
                    adapter.submitList(viewModel.editableCategoriesList)
                } else {
                    viewModel.editableCategoriesList = emptyList<FoodCategoryApp>().toMutableList()
                }
            }
            updateRecipeSuccess.observe(viewLifecycleOwner) {
                if (it != null) {
                    activity.showPopup(
                        0,
                        getString(R.string.success),
                        message = getString(R.string.recipe_saved_successfully)
                    )
                    binding.lytImportedRecipeUpdate.btnSave.visibility = INVISIBLE
                    binding.lytImportedRecipeUpdate.btnDelete.visibility = INVISIBLE
                    binding.lytImportedRecipeUpdate.btnUpdate.visibility = INVISIBLE
                    navigateBackToMyRecipe()
                } else {
                    activity.showPopup(
                        1,
                        getString(R.string.error),
                        getString(R.string.error_occured_while_saving_the_recipe)
                    )
                }
            }
            gotRequestedRecipe.observe(viewLifecycleOwner) {
                if (it != null) {
                    activity.showPopup(
                        0,
                        getString(R.string.success),
                        message = getString(R.string.got_recipe)
                    )
                    viewModel.recipeFromArgs = it
                    fillDetailsWithReceivedRecipe()
                    viewModel.getCategoryList()
                } else {
                    activity.showPopup(
                        1,
                        getString(R.string.error),
                        getString(R.string.error_occured_while_getting_the_recipe)
                    )
                }
            }
            nothingToUpdate.observe(viewLifecycleOwner) {
                if (it) {
                    activity.showPopup(
                        2,
                        getString(R.string.info_nothing),
                        message = getString(R.string.nothing_to_update)
                    )
                }
            }
            serveCount.observe(viewLifecycleOwner) {
                binding.lytImportedRecipeUpdate.metServingCount.text =
                    Editable.Factory.getInstance().newEditable(it.toString())
            }
            cookingTime.observe(viewLifecycleOwner) {
                binding.lytImportedRecipeUpdate.metCookingTime.text =
                    Editable.Factory.getInstance().newEditable(it.toString())
            }
        }
    }

    private fun navigateBackToMyRecipe() {
        requireActivity().onBackPressed()
    }

    /**
     * Fills the UI fields with details obtained from a received recipe.
     * If the recipe contains a photo, it sets the image bitmap in the ImageView.
     * Populates various UI fields such as recipe name, serving count, cooking time,
     * ingredients, instructions, and categories based on the received recipe data.
     */
    private fun fillDetailsWithReceivedRecipe() {
        // Check if the recipe has a photo and set it in the ImageView if available
        if (viewModel.recipeFromArgs?.photo != null) {
            val imageBitmap =
                BitmapFactory.decodeByteArray(
                    viewModel.recipeFromArgs!!.photo as ByteArray?,
                    0,
                    (viewModel.recipeFromArgs!!.photo as ByteArray).size
                )
            binding.lytImportedRecipeUpdate.ivChooseItemImg.setImageBitmap(imageBitmap)
        }
        // Apply recipe details to UI fields
        binding.lytImportedRecipeUpdate.apply {
            // Set recipe name field if available
            if (viewModel.recipeFromArgs != null)
                etRecipeName.editText?.setText(viewModel.recipeFromArgs!!.recipeName.toString())
            // Set serving count field
            metServingCount.setText(viewModel.recipeFromArgs!!.serving.toString())
            // Set cooking time field
            metCookingTime.setText(viewModel.recipeFromArgs!!.preparationTime.toString())
            // Populate ingredient fields
            val ingredientString = viewModel.recipeFromArgs!!.ingredients.split("\n")
            for (i in ingredientString) {
                setIngredientField(i)
            }
            // Populate instruction fields
            val instructionString = viewModel.recipeFromArgs!!.instruction.split("\n")
            for (i in instructionString) {
                setInstructionsField(i)
            }

        }
        viewModel.recipeFromArgs?.serving?.let {
            viewModel.setDefaultCount(
                TagConstant.TAG_SERVE_COUNT,
                it.toInt()
            )
        }
        viewModel.recipeFromArgs?.preparationTime?.let {
            viewModel.setDefaultCount(
                TagConstant.TAG_COOKING_TIME,
                it.toInt()
            )
        }
    }

    /**
     * Initiates the process of saving the recipe.
     */
    private fun callToUpdateRecipe() {

        val uiRecipeName = binding.lytImportedRecipeUpdate.etRecipeName.editText?.text.toString()
        val uiPreparationTime =
            binding.lytImportedRecipeUpdate.metCookingTime.text.toString().toInt()

        val uiServing = binding.lytImportedRecipeUpdate.metServingCount.text.toString().toInt()
        val uiPhoto = viewModel.imageBytes

        processIfInstructionsChanged()
        val uiInstructions = viewModel.instructionsList.joinToString("\n")
        viewModel.recipeFromArgs?.instruction = uiInstructions

        val foodCategories = ArrayList<FoodCategory>().toMutableList()
        for (i in viewModel.editableCategoriesList) {
            if (i.isSelected) {
                foodCategories.add(FoodCategory(i.idFoodCategory, i.categoryName))
            }
        }
        viewModel.recipeFromArgs?.categories = foodCategories

        if (uiRecipeName.equals(viewModel.recipeFromArgs?.recipeName, ignoreCase = false)) {
            viewModel.recipeFromArgs?.recipeName = uiRecipeName
        }
        if (uiServing != (viewModel.recipeFromArgs?.serving ?: 0)) {
            viewModel.recipeFromArgs?.serving = uiServing
        }
        if (uiPreparationTime != (viewModel.recipeFromArgs?.preparationTime ?: 0)) {
            viewModel.recipeFromArgs?.preparationTime = uiPreparationTime
        }
        if (viewModel.imageBytes != null) {
            viewModel.recipeFromArgs?.photo = uiPhoto
        }

        viewModel.updateRecipe(10, viewModel.recipeFromArgs)
    }

    /**
     * Adds a new ingredient field to the UI.
     */
    private fun setIngredientField(ingredientString: String) {
        val subBinding = LayoutDuplicateIngredientItemBinding.inflate(layoutInflater)
        subBinding.btnDeleteIngredient.setOnClickListener {
            binding.lytImportedRecipeUpdate.lytDynamicIngredients.removeView(subBinding.root)
        }

        val view = subBinding.root
        view.findViewById<TextInputEditText>(R.id.metDynamicInserter).text =
            Editable.Factory.getInstance().newEditable(ingredientString)
        binding.lytImportedRecipeUpdate.lytDynamicIngredients.addView(view)
    }

    /**
     * Adds a new instruction field to the UI.
     */
    private fun setInstructionsField(instructionString: String) {
        val subBinding = LayoutDuplicateIngredientItemBinding.inflate(layoutInflater)
        subBinding.btnDeleteIngredient.setOnClickListener {
            binding.lytImportedRecipeUpdate.lytDynamicInstructions.removeView(subBinding.root)
        }

        val view = subBinding.root
        view.findViewById<TextInputEditText>(R.id.metDynamicInserter).text =
            Editable.Factory.getInstance().newEditable(instructionString)
        binding.lytImportedRecipeUpdate.lytDynamicInstructions.addView(view)
    }

    /**
     * Adds a new ingredient field to the UI.
     */
    private fun addIngredientField() {
        val subBinding = LayoutDuplicateIngredientItemBinding.inflate(layoutInflater)
        subBinding.btnDeleteIngredient.setOnClickListener {
            binding.lytImportedRecipeUpdate.lytDynamicIngredients.removeView(subBinding.root)
        }

        if (binding.lytImportedRecipeUpdate.lytDynamicIngredients.childCount > 0) {
            for (i in binding.lytImportedRecipeUpdate.lytDynamicIngredients.children) {
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
        binding.lytImportedRecipeUpdate.lytDynamicIngredients.addView(view)
    }

    /**
     * Adds a new instruction field to the UI.
     */
    private fun addInstructionsField() {
        val subBinding = LayoutDuplicateIngredientItemBinding.inflate(layoutInflater)
        subBinding.btnDeleteIngredient.setOnClickListener {
            binding.lytImportedRecipeUpdate.lytDynamicInstructions.removeView(subBinding.root)
        }

        if (binding.lytImportedRecipeUpdate.lytDynamicInstructions.childCount > 0) {
            for (i in binding.lytImportedRecipeUpdate.lytDynamicInstructions.children) {
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
        binding.lytImportedRecipeUpdate.lytDynamicInstructions.addView(view)
    }

    /**
     * Validates recipe details before saving.
     */
    private fun validateRecipeDetails() {
        val errorString =
            if (binding.lytImportedRecipeUpdate.etRecipeName.editText?.text.toString().isBlank()) {
                getString(R.string.warn_please_add_a_recipe_name)
            } else if (binding.lytImportedRecipeUpdate.metServingCount.text.toString()
                    .isBlank() || binding.lytImportedRecipeUpdate.metServingCount.text.toString() == getString(
                    R.string.default_zero
                )
            ) {
                getString(R.string.warn_please_add_a_serving_count)
            } else if (binding.lytImportedRecipeUpdate.metCookingTime.text.toString()
                    .isBlank() || binding.lytImportedRecipeUpdate.metCookingTime.text.toString() == getString(
                    R.string.default_zero
                )
            ) {
                getString(R.string.warn_please_add_a_cooking_time)
            } else if (viewModel.editableCategoriesList.size < 1) {
                getString(R.string.warn_please_choose_atleast_a_category)
            } else if (binding.lytImportedRecipeUpdate.lytDynamicIngredients.childCount < 1) {
                getString(R.string.warn_please_add_atleast_an_ingredient)
            } else if (binding.lytImportedRecipeUpdate.lytDynamicInstructions.childCount < 1) {
                getString(R.string.warn_please_add_atleast_an_instruction)
            } else {
                null
            }

        if (errorString == null) {
            processIfIngredientsChanged()
        } else {
            Snackbar.make(
                requireContext(),
                binding.lytImportedRecipeUpdate.btnSave,
                errorString,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun processIfIngredientsChanged() {
        if (binding.lytImportedRecipeUpdate.lytDynamicIngredients.childCount > 0) {
            for (i in 0 until binding.lytImportedRecipeUpdate.lytDynamicIngredients.childCount) {
                val childView = binding.lytImportedRecipeUpdate.lytDynamicIngredients.getChildAt(i)
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
        val comparingIngredients = viewModel.ingredientsList.joinToString("\n")
        if (!viewModel.recipeFromArgs?.ingredients.equals(
                comparingIngredients,
                ignoreCase = true
            )
        ) {
            viewModel.recipeFromArgs?.ingredients = comparingIngredients
            val ingredients = viewModel.ingredientsList.joinToString(" and ")
            viewModel.getNutritionsVm(ingredients)
        } else {
            callToUpdateRecipe()
        }

    }


    private fun processIfInstructionsChanged() {
        if (binding.lytImportedRecipeUpdate.lytDynamicInstructions.childCount > 0) {
            for (i in 0 until binding.lytImportedRecipeUpdate.lytDynamicInstructions.childCount) {
                val childView = binding.lytImportedRecipeUpdate.lytDynamicInstructions.getChildAt(i)
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

    }

    /**
     * Calls the API to fetch nutrition information based on ingredients.
     */
    private fun callNutrientsApi() {
        if (binding.lytImportedRecipeUpdate.lytDynamicIngredients.childCount > 0) {
            for (i in 0 until binding.lytImportedRecipeUpdate.lytDynamicIngredients.childCount) {
                val childView = binding.lytImportedRecipeUpdate.lytDynamicIngredients.getChildAt(i)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}