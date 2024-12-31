package com.marcohuijskes.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.marcohuijskes.runningapp.R
import com.marcohuijskes.runningapp.databinding.FragmentRunBinding
import com.marcohuijskes.runningapp.databinding.FragmentStatisticsBinding
import com.marcohuijskes.runningapp.ui.viewmodels.MainViewModel
import com.marcohuijskes.runningapp.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private lateinit var binding: FragmentStatisticsBinding

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticsBinding.bind(view)
    }

}