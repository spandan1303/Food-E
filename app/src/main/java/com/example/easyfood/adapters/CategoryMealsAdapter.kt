package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.pojo.MealsByCategory

class CategoryMealsAdapter: RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {

    private val mealsList = ArrayList<MealsByCategory>()

    fun setMealsList(mealsList: List<MealsByCategory>) {
        this.mealsList.clear()
        this.mealsList.addAll(mealsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        val binding = MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryMealsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        val meal = mealsList[position]
        Glide.with(holder.itemView.context)
            .load(meal.strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    class CategoryMealsViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)
}
