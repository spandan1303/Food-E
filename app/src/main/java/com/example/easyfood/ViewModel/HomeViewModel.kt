package com.example.easyfood.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.pojo.*
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class HomeViewModel (
    private val mealDatabase: MealDatabase
        ): ViewModel() {

    private val randomMealLiveData = MutableLiveData<Meal>()
    private val popularItemsLiveData=MutableLiveData<List<MealsByCategory>>()
    private val categoriesLiveData=MutableLiveData<List<Category>>()
    private var favoritesMealLiveData=mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData=MutableLiveData<Meal>()
    private val searchedMealsLiveData=MutableLiveData<List<Meal>>()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList?> {
            override fun onResponse(call: Call<MealList?>, response: Response<MealList?>) {
                response.body()?.let { mealList ->
                    val randomMeal: Meal = mealList.meals.firstOrNull() ?: return
                    // Log.d("Test", "meal id ${randomMeal.idMeal} name ${randomMeal.strMeal}")
                    randomMealLiveData.postValue(randomMeal)  // Use postValue to ensure it's thread-safe
                } ?: run {
                    Log.d("HomeFragment", "Response body is null")
                }
            }

            override fun onFailure(call: Call<MealList?>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItem("Seafood").enqueue(object : Callback<MealsByCategoryList?> {
            override fun onResponse(call: Call<MealsByCategoryList?>, response: Response<MealsByCategoryList?>) {
                if(response.body()!=null){
                    popularItemsLiveData.value=response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList?>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }
        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList?> {
            override fun onResponse(call: Call<CategoryList?>, response: Response<CategoryList?>) {
                if(response.body()!=null){
                    categoriesLiveData.value=response.body()!!.categories
                }
            }

            override fun onFailure(call: Call<CategoryList?>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }
        })
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().update(meal)
        }
    }

    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList?> {
            override fun onResponse(call: Call<MealList?>, response: Response<MealList?>) {
                val meal=response.body()?.meals?.first()
                meal?.let {
                    bottomSheetMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun searchMeal(searchQuery: String){
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList?> {
            override fun onResponse(call: Call<MealList?>, response: Response<MealList?>) {
                val mealList=response.body()?.meals
                mealList?.let {
                    searchedMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun observeSearchedMealsLiveData():LiveData<List<Meal>>{
        return searchedMealsLiveData
    }

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }
    fun observePopularItemLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }
    fun observeCategoriesLiveData(): LiveData<List<Category>>{
        return categoriesLiveData
    }
    fun observeFavoritesMealsLiveData():LiveData<List<Meal>>{
        return favoritesMealLiveData
    }
    fun observeBottomSheetMeal():LiveData<Meal>{
        return bottomSheetMealLiveData
    }

}
