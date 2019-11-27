package uk.co.applylogic.marvel.data.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.applylogic.marvel.data.model.MarvelBaseResponse
import kotlin.collections.ArrayList

interface ContentInterface {
    //    https://gateway.marvel.com/v1/public/comics
    //    ?ts=1
    //    &apikey=3d3ce5daa8ec0f7c17afc52bb68f15f7
    //    &hash=a45bdb0bf57b06e72ad4c2c5854e2843
    @GET("v1/public/comics")
    suspend fun getContent(
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("limit") loadSize: Int,
        @Query("offset") offset: Int,
        @Query("title") title: String?
    )
            : Response<MarvelBaseResponse>?
}

interface ContentRepositoryContract {

    suspend fun getCachedContent(): ArrayList<String>?

}

class ContentRepository : ContentRepositoryContract {

    override suspend fun getCachedContent(): ArrayList<String>? {
        return null
    }
}

