package com.example.foodrecipe.remote

import com.example.foodrecipe.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject



class RemoteDataSource @Inject constructor(
        private val foodRecipesApi: FoodRecipesApi
        //hilt will search for this specific type in Network Module,and search for function
        //which return same type
) {
    suspend fun getRecipes(quaries:Map<String,String>):Response<FoodRecipe>{
        return  foodRecipesApi.getRecipes(quaries)
    }
}