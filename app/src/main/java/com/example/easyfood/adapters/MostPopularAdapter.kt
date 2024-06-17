package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.PopularItemsBinding
import com.example.easyfood.pojo.MealsByCategory

class MostPopularAdapter():RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    lateinit var onItemsClick:((MealsByCategory)->Unit)
    private var mealList=ArrayList<MealsByCategory>()
    var onLongItemClick:((MealsByCategory)->Unit)?=null

    fun setMeals(mealList: ArrayList<MealsByCategory>){
        this.mealList=mealList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        val itemView=PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  PopularMealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)


        holder.itemView.setOnClickListener{
            onItemsClick.invoke(mealList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class PopularMealViewHolder( val binding: PopularItemsBinding):RecyclerView.ViewHolder(binding.root)
}