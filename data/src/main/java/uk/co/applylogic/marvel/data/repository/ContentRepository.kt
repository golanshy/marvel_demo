package uk.co.applylogic.marvel.data.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.applylogic.marvel.core_android.ktx.md5
import uk.co.applylogic.marvel.data.BuildConfig
import uk.co.applylogic.marvel.data.model.MarvelBaseResponse
import uk.co.applylogic.marvel.data.model.MarvelData

const val LOAD_SIZE = 25
const val TS = "1" // Force const timestamp to allow http caching

interface ContentInterface {

    @GET("v1/public/comics")
    suspend fun getContent(
        @Query("ts") ts: String = TS,
        @Query("apikey") apikey: String = BuildConfig.MARVEL_PUBLIC_API_KEY,
        @Query("hash") hash: String =
            "$ts${BuildConfig.MARVEL_PRIVATE_API_KEY}${BuildConfig.MARVEL_PUBLIC_API_KEY}".md5(),
        @Query("limit") limit: Int = LOAD_SIZE,
        @Query("offset") offset: Int,
        @Query("title") title: String?,
        @Query("orderBy") orderBy: String? = "-onsaleDate"
    )
            : Response<MarvelBaseResponse>?

    @GET("v1/public/comics/{comicId}")
    suspend fun getComicById(
        @Path("comicId") comicId: Int?,
        @Query("ts") ts: String = TS,
        @Query("apikey") apikey: String = BuildConfig.MARVEL_PUBLIC_API_KEY,
        @Query("hash") hash: String =
            "$ts${BuildConfig.MARVEL_PRIVATE_API_KEY}${BuildConfig.MARVEL_PUBLIC_API_KEY}".md5()
    )
            : Response<MarvelBaseResponse>?
}

interface ContentRepositoryContract {
    suspend fun cacheContent(data: MarvelData?)
    suspend fun getCachedContent(): MarvelData?
    suspend fun getCachedContentById(comicId: Int): MarvelData?
}

class ContentRepository : ContentRepositoryContract {

    override suspend fun cacheContent(data: MarvelData?) {
    }

    override suspend fun getCachedContent(): MarvelData? {
        return null
    }

    override suspend fun getCachedContentById(comicId: Int): MarvelData? {
        return null
    }
}

