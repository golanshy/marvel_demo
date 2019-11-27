package uk.co.applylogic.marvel.data.model

data class MarvelBaseResponse(
    val code: Int?,
    val status: String?,
    val copyright: String?,
    val attributionText: String?,
    val attributionHTML: String?,
    val etag: String?,
    val data: MarvelData?
)

data class MarvelData(
    val offset: Int?,
    val limit: Int?,
    val total: Int?,
    val count: Int?,
    val results: ArrayList<MarvelResult>?
)

data class MarvelResult(
    val id: Int?,
    val digitalId: Int?,
    val title: String?,
    val issueNumber: Int?,
    val description: String?,
    val thumbnail: MarvelThumbnail?
)

data class MarvelThumbnail(
    val path: String?,
    val extension: String?
)


