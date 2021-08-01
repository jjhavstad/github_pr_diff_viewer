package com.jjhavstad.githubprdiffviewer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjhavstad.githubprdiffviewer.datasources.apis.PrApiDataSource
import com.jjhavstad.githubprdiffviewer.models.PrDiff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrDiffLiveData(
    private val prApiDataSource: PrApiDataSource
) : ViewModel() {
    private val _prDiffLiveData: MutableLiveData<PrDiff> = MutableLiveData()
    val prDiffLiveData: LiveData<PrDiff>
        get() = _prDiffLiveData
    private val _prDiffErrorLiveData: MutableLiveData<Unit> = MutableLiveData()
    val prDiffErrorLiveData: LiveData<Unit>
        get() = _prDiffErrorLiveData

    fun requestPrDiffs(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                prApiDataSource.requestFileDiffs(path)?.let { _prDiff ->
                    _prDiffLiveData.postValue(_prDiff)
                }
            } catch (e: Exception) {
                _prDiffErrorLiveData.postValue(Unit)
            }
        }
    }
}
