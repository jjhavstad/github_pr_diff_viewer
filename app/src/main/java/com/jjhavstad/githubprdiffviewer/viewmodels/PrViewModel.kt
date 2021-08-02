package com.jjhavstad.githubprdiffviewer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjhavstad.githubprdiffviewer.datasources.apis.PrApiDataSource
import com.jjhavstad.githubprdiffviewer.models.Pr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrViewModel(
    private val prApiDataSource: PrApiDataSource
) : ViewModel() {
    private val _prLiveData: MutableLiveData<List<Pr>> = MutableLiveData()
    val prLiveData: LiveData<List<Pr>>
        get() = _prLiveData

    private val _prErrorLiveData: MutableLiveData<Unit> = MutableLiveData()
    val prErrorLiveData: LiveData<Unit>
        get() = _prErrorLiveData

    fun requestPrs(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                prApiDataSource.requestAllPrs(path)?.let { _prs ->
                    _prLiveData.postValue(_prs)
                }
            } catch (e: Exception) {
                _prErrorLiveData.postValue(Unit)
            }
        }
    }
}
