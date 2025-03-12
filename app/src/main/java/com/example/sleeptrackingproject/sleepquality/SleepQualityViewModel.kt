package com.example.sleeptrackingproject.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleeptrackingproject.database.SleepDatabaseDao
import com.example.sleeptrackingproject.database.SleepNight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepQualityViewModel(
    private val sleepNightKey: Long = 0L,
    val database: SleepDatabaseDao
) : ViewModel() {

    private val viewJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main+viewJob)

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    init {
        doneNavigating()
    }

    fun doneNavigating() {
        _navigateToSleepTracker.value = false
    }

    fun onSetSleepQuality(quality: Int) {
        uiScope.launch {
            println("in onSetSleepQuality ${quality}")
            val tonight = get(sleepNightKey)
            tonight.sleepQuality = quality

            _navigateToSleepTracker.value = true
            println("navigate to tracker ${_navigateToSleepTracker.value}")

            update(tonight)
        }
    }

    private suspend fun update(night: SleepNight){
        withContext(Dispatchers.IO){

            database.update(night)
        }
    }

    private suspend fun get(key: Long) : SleepNight{
        return withContext(Dispatchers.IO){
            database.get(key)
        }
    }



}