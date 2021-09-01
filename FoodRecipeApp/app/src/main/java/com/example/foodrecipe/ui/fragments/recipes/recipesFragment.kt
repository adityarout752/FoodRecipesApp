package com.example.foodrecipe.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipe.MainViewModel
import com.example.foodrecipe.R
import com.example.foodrecipe.adapters.RecipesAdapter
import com.example.foodrecipe.constants.Constants.Companion.API_KEY
import com.example.foodrecipe.constants.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*

@AndroidEntryPoint
class recipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mView: View
    private val mAdapter by lazy { RecipesAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_recipes, container, false)
        mainViewModel=ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
setUpRecyclerView()
        requestApiData()
        return mView
    }
private fun requestApiData(){
    mainViewModel.getRecipes(applyQuaries())
    mainViewModel.recipesResponse.observe(viewLifecycleOwner) {response->
        when(response){
            is NetworkResult.Success ->{
                hideShimmerEffect()
                response.data?.let {

                    Log.d("adi","${it}")
                    mAdapter.setData(it)
                }
            }
            is NetworkResult.Error -> {
                hideShimmerEffect()
                Log.d("adi","${response.message.toString()}")
                Toast.makeText(
                    requireContext(),
                    response.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is NetworkResult.Loading ->{
                showShimmerEffect()
            }
        }
    }
}
    private fun applyQuaries():HashMap<String,String>{
        val quaries:HashMap<String,String> = HashMap()
        quaries["number"]="50"
        quaries["apiKey"]=API_KEY
        quaries["diet"]="vegan"
        quaries["addRecipeInformation"]="true"
        quaries["fillIngredients"]="true"
        return quaries

    }
    private fun setUpRecyclerView(){
        mView.Recyclerview.adapter=mAdapter
        mView.Recyclerview.layoutManager=LinearLayoutManager(requireContext())
        showShimmerEffect()
    }
private fun showShimmerEffect(){
    mView.Recyclerview.showShimmer()
}
    private fun hideShimmerEffect(){
        mView.Recyclerview.hideShimmer()
    }


}