package com.marcohuijskes.runningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.marcohuijskes.runningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {
    
}