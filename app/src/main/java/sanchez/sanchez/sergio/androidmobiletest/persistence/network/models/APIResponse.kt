package sanchez.sanchez.sergio.androidmobiletest.persistence.network.models

import com.squareup.moshi.Json

/**
 * Generic API Response Wrapper
 **/
data class APIResponse<T> (
    // The HTTP status code of the returned result.
    @field:Json(name = "code") val code: String,
    // A string description of the call status.
    @field:Json(name = "status") val status: String,
    // The results returned by the call.
    @field:Json(name = "data") val data: DataResult<T>
)

/**
 * Data Result Wrapper
 */
data class DataResult<T>(
    // The requested offset (number of skipped results) of the call.
    @field:Json(name = "offset") val offset: Int,
    // The requested result limit.
    @field:Json(name = "limit") val limit: Int,
    // The total number of resources available given the current filter set.
    @field:Json(name = "total") val total: Int,
    // The total number of results returned by this call.
    @field:Json(name = "count") val count: Int,
    // The list of characters returned by the call.
    @field:Json(name = "results") val results: List<T>
)