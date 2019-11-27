package uk.co.applylogic.marvel.feature_marvel.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import uk.co.applylogic.marvel.data.BuildConfig
import uk.co.applylogic.marvel.data.model.MarvelResult
import uk.co.applylogic.marvel.data.model.UIState
import uk.co.applylogic.marvel.core_android.ktx.md5
import uk.co.applylogic.marvel.data.model.MarvelBaseResponse
import uk.co.applylogic.marvel.feature_marvel.di.ContentComponent
import java.util.*
import kotlin.collections.ArrayList

class MarvelMainViewModel : ViewModel() {

    internal lateinit var comp: ContentComponent
    var uiState: MutableLiveData<UIState> = MutableLiveData(UIState.Initialized)
    var searchResults: MutableLiveData<ArrayList<MarvelResult>> = MutableLiveData(arrayListOf())

    fun getContent(title: String?) {

        uiState.value = UIState.InProgress
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ts = Date().time.toString()
                processResponse(
                    comp.contentInterface().getContent(
                        ts,
                        BuildConfig.MARVEL_PUBLIC_API_KEY,
                        "$ts${BuildConfig.MARVEL_PRIVATE_API_KEY}${BuildConfig.MARVEL_PUBLIC_API_KEY}".md5(),
                        25, 0,
                        title
                    )
                )
                // withContext(Dispatchers.Main) {}
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.postValue(UIState.Error(0, e.localizedMessage))
            }
        }
    }

    private fun processResponse(response: Response<MarvelBaseResponse>?) {

        uiState.postValue(
            if (response?.body()?.data?.results.isNullOrEmpty())
                UIState.NoResults
            else
                UIState.OnResults
        )
        response?.body()?.data?.results?.let {
            searchResults.postValue(it)
        }
        response?.errorBody()?.let {
            uiState.postValue(UIState.Error(response.code(), response.message()))
        }
    }
}
