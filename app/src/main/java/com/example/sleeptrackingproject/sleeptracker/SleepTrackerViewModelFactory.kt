package com.example.sleeptrackingproject.sleeptracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sleeptrackingproject.database.SleepDatabaseDao

class SleepTrackerViewModelFactory (private val dataSource : SleepDatabaseDao,
                                    private val context: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SleepTrackerViewModel::class.java)){
            return SleepTrackerViewModel(dataSource,context) as T
        }

        throw IllegalArgumentException("Invalid ViewModel Class")
    }
}