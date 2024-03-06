package com.gtp01.group01.android.recipesmobileapp.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Object providing test data for recipe lists.
 */
object RecipeListTestData {
    /**
     * Retrieves a list of recipe objects for testing purposes.
     *
     * @return List<Recipe>: A list of recipe objects.
     */
    fun getRecipeList(): List<Recipe> {
        val jsonString = """
            [
              {
                "idrecipe": 1,
                "owner": {
                  "iduser": 10,
                  "email": "jack@email.com",
                  "fullname": "Jack Bower",
                  "preferCategories": [
                    {
                      "idfoodCategory": 1,
                      "categoryName": "Italian"
                    },
                    {
                      "idfoodCategory": 2,
                      "categoryName": "Pasta"
                    }
                  ],
                  "preferDuration": 60,
                  "preferCalory": 2000
                },
                "recipeName": "Spaghetti Bolognese",
                "ingredients": "Ground beef\\ntomatoes\\nonions\\ngarlic\\npasta",
                "instruction": "1. Brown the beef\\n2. Add onions and garlic\\n",
                "preparationTime": 30,
                "calory": 100,
                "protein": 200,
                "carbs": 50,
                "serving": 4,
                "photo": null,
                "categories": [
                  {
                    "idfoodCategory": 1,
                    "categoryName": "Italian"
                  },
                  {
                    "idfoodCategory": 2,
                    "categoryName": "Pasta"
                  }
                ],
                "isActive": 1,
                "likeCount": 1,
                "hasLike": false,
                "hasFavorite": false
              },
              {
                "idrecipe": 3,
                "owner": {
                  "iduser": 11,
                  "email": "pp@h.com",
                  "fullname": "wtwtwt",
                  "preferCategories": [],
                  "preferDuration": 0,
                  "preferCalory": 0
                },
                "recipeName": "Margarita",
                "ingredients": "Ground beef\\ntomatoes\\nonions\\ngarlic\\npasta",
                "instruction": "1. Brown the beef\\n2. Add onions and garlic\\n",
                "preparationTime": 30,
                "calory": 100,
                "protein": 200,
                "carbs": 50,
                "serving": 4,
                "photo": null,
                "categories": [
                  {
                    "idfoodCategory": 1,
                    "categoryName": "Italian"
                  }
                ],
                "isActive": 1,
                "likeCount": 0,
                "hasLike": false,
                "hasFavorite": false
              }
            ]
        """.trimIndent()
        val gson = Gson()
        return gson.fromJson<List<Recipe>?>(jsonString, object : TypeToken<List<Recipe>>() {}.type)
    }
}