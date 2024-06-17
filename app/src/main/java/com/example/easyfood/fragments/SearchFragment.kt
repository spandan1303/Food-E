package com.example.easyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easyfood.R
import com.example.easyfood.ViewModel.HomeViewModel
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.adapters.FavoritesMealsAdapter
import com.example.easyfood.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter:FavoritesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=(activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.imgSearchArrow.setOnClickListener {
            searchMeals()
        }

        observeSearchedMealsLiveData()
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner,Observer{mealsList->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        })
    }

    private fun searchMeals() {
        val searchQuery=binding.etSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeal(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter= FavoritesMealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=searchRecyclerViewAdapter
        }
    }

}