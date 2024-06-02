package com.example.hydroheroapp.view.main.analyze

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.hydroheroapp.R
import com.example.hydroheroapp.databinding.FragmentAnalyzeBinding

class AnalyzeFragment : Fragment() {
    private  var _binding: FragmentAnalyzeBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, gender)
        binding.autoCompleteTextview.setAdapter(arrayAdapter)
    }



}