package uk.co.applylogic.marvel.feature_marvel.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.Response
import uk.co.applylogic.marvel.data.model.MarvelResult
import uk.co.applylogic.marvel.data.model.UIState
import uk.co.applylogic.marvel.data.model.MarvelBaseResponse
import uk.co.applylogic.marvel.data.model.MarvelData
import uk.co.applylogic.marvel.feature_marvel.di.ContentComponent
import java.io.IOException
import kotlin.collections.ArrayList

class MarvelMainViewModel : ViewModel() {

    internal lateinit var comp: ContentComponent
    var uiState: MutableLiveData<UIState> = MutableLiveData(UIState.Initialized)
    var searchTerm: LiveData<String>? = null
    var searchResults: MutableLiveData<ArrayList<MarvelResult>> = MutableLiveData(arrayListOf())
    var selectedResult: MutableLiveData<MarvelResult> = MutableLiveData()
    internal var offset = 0

    fun getContent() {

        uiState.value = UIState.InProgress
        viewModelScope.launch {
            try {
                processResponse(
                    comp.contentInterface().getContent(
                        offset = offset,
                        title = searchTerm?.value
                    )
                )
                // withContext(Dispatchers.Main) {}
            } catch (e: IOException) {
                e.printStackTrace()
                uiState.postValue(UIState.Error(0, e.localizedMessage))

                comp.contentRepository().getCachedContent()?.let {
                    processData(it)
                }
            }
        }
    }

    fun onItemSelected(comicId: Int?) {
        comicId?.let {
            uiState.value = UIState.InProgress
            viewModelScope.launch {
                try {
                    processResponse(
                        comp.contentInterface().getComicById(comicId = it)
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                    uiState.postValue(UIState.Error(0, e.localizedMessage))

                    comp.contentRepository().getCachedContentById(comicId = it)?.let {
                        processData(it)
                    }
                }
            }
        }
    }

    private fun processResponse(response: Response<MarvelBaseResponse>?) {
        response?.errorBody()?.let {
            uiState.postValue(UIState.Error(response.code(), response.message()))
        }

        response?.body()?.let { body ->
            processData(body.data)
            viewModelScope.launch {
                comp.contentRepository().cacheContent(body.data)
            }
        }
    }

    private fun processData(data: MarvelData?) {
        uiState.postValue(
            if (offset == 0 && data?.results.isNullOrEmpty())
                UIState.NoResults
            else
                UIState.OnResults
        )

        data?.results?.let { results ->
            if (results.size == 1) {
                selectedResult.postValue(results[0])
            } else {
                offset = data.offset!! + data.count!!
                searchResults.postValue(results)
            }
        }
    }
}
