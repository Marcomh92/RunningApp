package com.marcohuijskes.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.marcohuijskes.runningapp.R
import com.marcohuijskes.runningapp.databinding.FragmentSetupBinding

class SetupFragment : Fragment(R.layout.fragment_setup) {

    private lateinit var binding: FragmentSetupBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupBinding.bind(view)
        binding.tvContinue.setOnClickListener {
            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
        }
    }
}