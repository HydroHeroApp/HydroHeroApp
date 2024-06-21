package com.example.hydroheroapp.view.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hydroheroapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            requireContext().getSharedPreferences("HydroHeroPrefs", Context.MODE_PRIVATE)
        val savedIntake = sharedPreferences.getInt("currentIntake", 0)
        val savedTarget = sharedPreferences.getInt("totalIntake", 3000)

        binding.waterIntakeView.setIntake(savedIntake)
        binding.waterIntakeView.setTotalIntake(savedTarget)

        binding.btnAdd.setOnClickListener {
            val amountToAdd = binding.etAddAmount.text.toString().toIntOrNull()
            if (amountToAdd != null && amountToAdd > 0) {
                binding.waterIntakeView.addIntake(amountToAdd)
                Toast.makeText(context, "$amountToAdd ml added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}