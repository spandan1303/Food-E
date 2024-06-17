package com.example.easyfood.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.R
import com.example.easyfood.ViewModel.CategoryMealsViewModel
import com.example.easyfood.adapters.CategoryMealsAdapter
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.fragments.HomeFragment

class CategoryMealsActivity : AppCompatActivity() {

    lateinit var binding:ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel:CategoryMealsViewModel
    lateinit var categoryMealsAdapter:CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel= ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this,Observer{mealsList->

            binding.tvCategoryCount.text="No.of meals in this Category are: ${mealsList.size.toString()}"
           categoryMealsAdapter.setMealsList(mealsList)

        })

    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter=CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=categoryMealsAdapter
        }
    }
}