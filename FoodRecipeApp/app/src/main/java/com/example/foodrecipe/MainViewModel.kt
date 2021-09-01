package com.example.foodrecipe

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipe.constants.NetworkResult
import com.example.foodrecipe.model.FoodRecipe
import com.example.foodrecipe.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

//we donot need viewmodel factory to pass repository,everything will be handled by VIEWMODELINJECT
class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application):AndroidViewModel(application) {

    var recipesResponse:MutableLiveData<NetworkResult<FoodRecipe>> =MutableLiveData()
fun getRecipes(queries:Map<String,String>)=viewModelScope.launch {
    getaRecipesSafeCall(queries)
}

    private suspend fun getaRecipesSafeCall(queries: Map<String, String>) {
recipesResponse.value=NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
        val response=repository.remote.getRecipes(queries)
                recipesResponse.value=handleFoodRecipesResponse(response)
            }catch (e:Exception){
recipesResponse.value=NetworkResult.Error("Recipes Not Found")
            }
        }else{
            recipesResponse.value=NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection():Boolean{
            val connectivityManager=getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            )as ConnectivityManager

            val activeNetwork=connectivityManager.activeNetwork?:return false
            val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                else->false
            }
        }
}