package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.ViewModel.HomeViewModel
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.adapters.MostPopularAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.Meal

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter


    companion object{
        const val MEAL_ID="com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME="com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB="com.example.easyfood.fragments.thumbMeal"
        const val CATEGORY_NAME="om.example.easyfood.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //when 3 instances of a viewmodel is not req go to main activity and instantiate
        //homeMvvm = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel=(activity as MainActivity).viewModel
        popularItemsAdapter= MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemLiveData()

        preparePopularItemRecyclerView()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClicked()
        onPopularItemLongClick()
        onSearchIconClick()

    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick={meal->
            val mealBottomSheetFragment=MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClicked() {
        categoriesAdapter.onItemClick={ category ->
            val intent=Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }

    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter= CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager= GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            // Update the adapter with the new list of categories
            categoriesAdapter.setCategories(categories as ArrayList<Category>)
        })
    }


    private fun onPopularItemClick() {
        popularItemsAdapter.onItemsClick={meal ->
            val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter=popularItemsAdapter
        }
    }

    private fun observePopularItemLiveData() {
        viewModel.observePopularItemLiveData().observe(viewLifecycleOwner
        ) { mealsList->
                popularItemsAdapter.setMeals(mealList= mealsList as ArrayList<MealsByCategory>)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, Observer { meal ->
            meal?.let {
                Glide.with(this@HomeFragment)
                    .load(it.strMealThumb)
                    .into(binding.imgRandomMeal)

                //randomMeal getting all the attributes of meal
                this.randomMeal=meal

            } ?: run {
                Log.e("HomeFragment", "Random meal is null")
            }
        })
    }
}