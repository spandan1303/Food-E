package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.ViewModel.HomeViewModel
import com.example.easyfood.ViewModel.MealViewModel
import com.example.easyfood.ViewModel.MealViewModelFactory
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Meal

class MealActivity : AppCompatActivity() {

    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var binding: ActivityMealBinding
    private lateinit var youTubeLink:String
    private lateinit var mealMvvm:MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase=MealDatabase.getInstance(this)
        val viewModelFactory=MealViewModelFactory(mealDatabase)
        mealMvvm= ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]
        //mealMvvm= ViewModelProvider(this).get(MealViewModel::class.java)

        getMealInformationFromIntent()

        setInformationInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observeMealDetailsLiveData()

        onYouTubeImageClick()

        onFavoriteClick()

    }

    private fun onFavoriteClick() {
        binding.btnAddToFavorites.setOnClickListener{
            mealToSave.let {
                if (it != null) {
                    mealMvvm.insertMeal(it)
                    Toast.makeText(this,"Your Meal Got Saved",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onYouTubeImageClick() {
        binding.imgYouTube.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW, Uri.parse(youTubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null
    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this,object : Observer<Meal>{
            override fun onChanged(value: Meal) {

                onResponseCase()
                val meal=value
                mealToSave=meal

                binding.tvCategory.text="Category : ${meal!!.strCategory}"
                binding.tvArea.text="Area : ${meal!!.strArea}"
                binding.textView2.text=meal.strInstructions

                youTubeLink=meal.strYoutube
            }

        })

    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title=mealName
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent= intent
        mealId=intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName=intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb=intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }
    private fun loadingCase(){
        binding.progressBar.visibility=View.VISIBLE
        binding.btnAddToFavorites.visibility= View.INVISIBLE
        binding.textView.visibility= View.INVISIBLE
        binding.tvCategory.visibility= View.INVISIBLE
        binding.tvArea.visibility=View.INVISIBLE
        binding.imgYouTube.visibility=View.INVISIBLE

    }

    private fun onResponseCase(){
        binding.progressBar.visibility=View.INVISIBLE
        binding.btnAddToFavorites.visibility= View.VISIBLE
        binding.textView.visibility= View.VISIBLE
        binding.tvCategory.visibility= View.VISIBLE
        binding.tvArea.visibility=View.VISIBLE
        binding.imgYouTube.visibility=View.VISIBLE
    }

}