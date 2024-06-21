package com.example.hydroheroapp.view.main.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hydroheroapp.R
import com.example.hydroheroapp.data.Result
import com.example.hydroheroapp.data.local.AnalysisHistory
import com.example.hydroheroapp.data.local.HistoryRepository
import com.example.hydroheroapp.data.remote.repository.LoginPrefsRepo
import com.example.hydroheroapp.data.remote.repository.dataStore
import com.example.hydroheroapp.databinding.FragmentAnalyzeBinding
import com.example.hydroheroapp.view.ViewModelFactory

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyRepository: HistoryRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.progressBar.visibility = View.GONE

        historyRepository = HistoryRepository(requireContext())

        val factory: ViewModelFactory = ViewModelFactory.getInstance(
            requireContext(),
            LoginPrefsRepo.getInstance(requireContext().dataStore)
        )

        val viewModel: AnalyzeViewModel = ViewModelProvider(this, factory)[AnalyzeViewModel::class.java]

        binding.btnAnalyze.setOnClickListener {
            val age = binding.edAge.text.toString()
            val gender = binding.edGender.text.toString()
            val weight = binding.edBodyWeight.text.toString()
            val height = binding.edBodyHeight.text.toString()
            val ch20 = binding.edWaterConsumption.text.toString()
            val faf = binding.edActivity.text.toString()

            viewModel.prediction(age, gender, weight, height, ch20, faf)
                .observe(viewLifecycleOwner) {
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val response = it.data
                                response.predict?.let { it1 -> saveToHistory(it1) }
                                AlertDialog.Builder(requireContext()).apply {
                                    setTitle(getString(R.string.analysis_results))
                                    setMessage(response.predict)
                                    setPositiveButton(getString(R.string.continue_dialog)) { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    create()
                                    show()
                                }
                            }

                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                AlertDialog.Builder(requireContext()).apply {
                                    setTitle(getString(R.string.error))
                                    setMessage(it.error)
                                    setPositiveButton(getString(R.string.continue_dialog)) { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    create()
                                    show()
                                }
                            }
                        }
                    }
                }
        }

        return root
    }

    private fun saveToHistory(result: String) {
        val history = historyRepository.getHistory().toMutableList()
        history.add(AnalysisHistory(result, System.currentTimeMillis()))
        historyRepository.saveHistory(history)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
