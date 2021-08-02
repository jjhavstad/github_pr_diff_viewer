package com.jjhavstad.githubprdiffviewer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjhavstad.githubprdiffviewer.datasources.apis.PrApiDataSource
import com.jjhavstad.githubprdiffviewer.models.PrDiff
import com.jjhavstad.githubprdiffviewer.models.PrDiffSplit
import com.jjhavstad.githubprdiffviewer.util.PrDiffSplitParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrDiffViewModel(
    private val prApiDataSource: PrApiDataSource
) : ViewModel() {
    private val prDiffSplitParser = PrDiffSplitParser()

    private val _prDiffLiveData: MutableLiveData<PrDiff> = MutableLiveData()
    val prDiffLiveData: LiveData<PrDiff>
        get() = _prDiffLiveData
    private val _prDiffSplitLiveData: MutableLiveData<PrDiffSplit> = MutableLiveData()
    val prDiffSplitLiveData: LiveData<PrDiffSplit>
        get() = _prDiffSplitLiveData
    private val _prDiffErrorLiveData: MutableLiveData<Unit> = MutableLiveData()
    val prDiffErrorLiveData: LiveData<Unit>
        get() = _prDiffErrorLiveData

    fun requestPrDiffs(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                prApiDataSource.requestFileDiffs(url)?.let { _prDiff ->
                    _prDiffLiveData.postValue(_prDiff)
                }
            } catch (e: Exception) {
                _prDiffErrorLiveData.postValue(Unit)
            }
        }
    }

    fun requestPrDiffSplits(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                prApiDataSource.requestFileDiffs(url)?.let { _prDiff ->
                    _prDiffSplitLiveData.postValue(
                        prDiffSplitParser.loadAndParse(_prDiff)
                    )
                }
            } catch (e: Exception) {
                _prDiffErrorLiveData.postValue(Unit)
            }
        }
    }
}
