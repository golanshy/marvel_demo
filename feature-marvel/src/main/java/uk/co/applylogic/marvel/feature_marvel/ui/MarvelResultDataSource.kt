package uk.co.applylogic.marvel.feature_marvel.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uk.co.applylogic.marvel.core_android.ktx.md5
import uk.co.applylogic.marvel.data.BuildConfig
import uk.co.applylogic.marvel.data.model.MarvelResult
import uk.co.applylogic.marvel.data.repository.ContentInterface
import java.util.*

class MarvelResultDataSource(
    private val contentInterface: ContentInterface,
    private val searchTerm: String?
) :
    PagingSource<Int, MarvelResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarvelResult> {
        val position = params.key ?: 1
        return try {

            val ts = Date().time.toString()
            val response = contentInterface.getContent(
                ts,
                BuildConfig.MARVEL_PUBLIC_API_KEY,
                "$ts${BuildConfig.MARVEL_PRIVATE_API_KEY}${BuildConfig.MARVEL_PUBLIC_API_KEY}".md5(),
                offset = position, title = searchTerm
            )

            val pagedResponse = response?.body()
            var nextKey = position
            pagedResponse?.data?.let {
                nextKey = it.offset + it.count
            }

            LoadResult.Page(
                data = pagedResponse?.data?.results.orEmpty(),
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MarvelResult>): Int? {
        return null
    }

}
