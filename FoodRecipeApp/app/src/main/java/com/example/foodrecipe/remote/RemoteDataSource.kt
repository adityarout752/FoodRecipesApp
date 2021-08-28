package com.example.foodrecipe.remote

import com.example.foodrecipe.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(quaries:Map<String,String>):Response<FoodRecipe>{
        return  foodRecipesApi.getRecipes(quaries)
    }
}