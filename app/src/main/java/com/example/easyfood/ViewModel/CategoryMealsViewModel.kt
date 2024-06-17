package com.example.easyfood.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.MealsByCategoryList
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {

    private val _mealsLiveData = MutableLiveData<List<MealsByCategory>>()
    val mealsLiveData: LiveData<List<MealsByCategory>>
        get() = _mealsLiveData

    fun getMealsByCategory(categoryName: String) {
        RetrofitInstance.api.getMealByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList?> {
            override fun onResponse(
                call: Call<MealsByCategoryList?>,
                response: Response<MealsByCategoryList?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { mealsList ->
                        _mealsLiveData.postValue(mealsList.meals)
                    }
                } else {
                    Log.e("CategoryMealsViewModel", "Response was not successful or body is null")
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList?>, t: Throwable) {
                Log.e("CategoryMealsViewModel", t.message.toString())
            }
        })
    }
    fun observeMealsLiveData():LiveData<List<MealsByCategory>>{
        return mealsLiveData
    }
}
