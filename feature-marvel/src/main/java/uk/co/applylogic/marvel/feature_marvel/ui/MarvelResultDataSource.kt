package uk.co.applylogic.marvel.feature_marvel.ui

import android.util.Log
import androidx.paging.PositionalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uk.co.applylogic.marvel.core_android.ktx.md5
import uk.co.applylogic.marvel.data.BuildConfig
import uk.co.applylogic.marvel.data.model.MarvelResult
import uk.co.applylogic.marvel.data.repository.ContentInterface
import java.util.*

class MarvelResultDataSource(
    private val scope: CoroutineScope,
    private val contentInterface: ContentInterface,
    private val title: String?
) :
    PositionalDataSource<MarvelResult>() {

    private val items = mutableListOf<MarvelResult>()
    private val loadSize = 25

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<MarvelResult>
    ) {
        val result = loadRangeInternal(0)
        callback.onResult(result.first, result.second, result.third)
    }

    private fun loadRangeInternal(position: Int): Triple<MutableList<MarvelResult>, Int, Int> {
        var totalCount = 0
        scope.launch {
            try {
                val ts = Date().time.toString()
                val response = contentInterface.getContent(
                    ts,
                    BuildConfig.MARVEL_PUBLIC_API_KEY,
                    "$ts${BuildConfig.MARVEL_PRIVATE_API_KEY}${BuildConfig.MARVEL_PUBLIC_API_KEY}".md5(),
                    loadSize = loadSize, offset = position, title = title
                )
                response?.let { r ->
                    if (r.isSuccessful) {
                        r.body()?.let { body ->
                            if (!body.data?.results.isNullOrEmpty()) {
                                totalCount = body.data?.total!!
                                this@MarvelResultDataSource.items.clear()
                                this@MarvelResultDataSource.items.addAll(items)
                            }
                        }
                    }
                }

            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data!")
            }
        }
        return Triple(this@MarvelResultDataSource.items, position, totalCount)
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<MarvelResult>
    ) {
        val result = loadRangeInternal(params.startPosition)
        callback.onResult(result.first)
    }
}
