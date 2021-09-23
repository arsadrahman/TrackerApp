package com.arsa.trackerapp.viewmodel

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arsa.trackerapp.repo.LocationRepository
import com.arsa.trackerapp.util.InternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    var internetStatus: MutableLiveData<Boolean> = MutableLiveData()
    fun updateInternetConnectionStatus()  {
        internetStatus.postValue(InternetConnection.isConnected(context))
    }
}