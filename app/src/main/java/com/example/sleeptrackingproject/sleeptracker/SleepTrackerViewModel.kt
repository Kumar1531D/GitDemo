package com.example.sleeptrackingproject.sleeptracker

import android.app.Application
import android.text.Spanned
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sleeptrackingproject.database.SleepDatabaseDao
import com.example.sleeptrackingproject.database.SleepNight
import com.example.sleeptrackingproject.formatNights
import kotlinx.coroutines.*

class SleepTrackerViewModel (private val database : SleepDatabaseDao,
                             private val application: Application
) : AndroidViewModel(application){

    private val viewJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main+viewJob)

    private var tonight = MutableLiveData<SleepNight?>()

    val nights = database.getAllNights()

    val navigateToSleepQuality = MutableLiveData<SleepNight?>()

//    val navigateToSleepQuality : LiveData<SleepNight>
//        get() = _navigateToSleepQuality

    private val _nightsString = MutableLiveData<Spanned>()
    val nightsString: LiveData<Spanned>
        get() = _nightsString

    private val _navigateToSleepDataQuality = MutableLiveData<Long>()
    val navigateToSleepDataQuality
        get() = _navigateToSleepDataQuality

    fun onSleepNightClicked(id: Long){
        _navigateToSleepDataQuality.value = id
    }

    fun onSleepDataQualityNavigated() {
        _navigateToSleepDataQuality.value = null
    }

    init {
        initialzeTonight()
        nights.observeForever { nightList ->
            _nightsString.value = formatNights(nightList, application.resources)
        }
    }

    fun doneNavigating(){
        navigateToSleepQuality.value = null
    }

    private fun initialzeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO){
            var night = database.getTonight()

            if(night?.startTimeMilli!=night?.endTimeMilli){
                night=null
            }
            night
        }
    }

    fun onStartTracking(){
        uiScope.launch {
            var newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night : SleepNight){
        withContext(Dispatchers.IO){
            database.insert(night)
        }
    }

    fun onStopTracking(){
        uiScope.launch {
            var oldNight = tonight.value ?: return@launch
            oldNight?.endTimeMilli = System.currentTimeMillis()

            update(oldNight)
            navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night : SleepNight){
        withContext(Dispatchers.IO){
            database.update(night)
        }
    }

    fun onClear(){
        uiScope.launch {
            clear()
            tonight.value = null
        }
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }


}