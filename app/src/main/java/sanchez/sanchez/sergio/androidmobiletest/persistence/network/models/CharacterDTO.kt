package sanchez.sanchez.sergio.androidmobiletest.persistence.network.models

import com.squareup.moshi.Json
import java.util.*


/**
 * Character DTO definition
 */

data class CharacterDTO (
    // The unique ID of the character resource.
    @field:Json(name = "id") val id: Long,
    // The name of the character
    @field:Json(name = "name") val name: String,
    // A short bio or description of the character
    @field:Json(name = "description") val description: String,
    // The date the resource was most recently modified. (example: 2018-07-19T21:33:19.000Z or invalid date format)
    @field:Json(name = "modified") val modified: Date?,
    // The representative image for this character.
    @field:Json(name = "thumbnail") val thumbnail: ThumbnailDTO,
    // A resource list containing comics which feature this character.
    @field:Json(name = "comics") val comics: ComicsDTO,
    // A resource list of series in which this character appears.
    @field:Json(name = "series") val series: ComicsDTO,
    // A resource list of events in which this character appears
    @field:Json(name = "events") val events: ComicsDTO
)

data class ComicsDTO (
    @field:Json(name = "available") val available: Long,
    @field:Json(name = "items") val items: List<ComicsItemDTO>,
    @field:Json(name = "returned") val returned: Long
)

data class ComicsItemDTO (
    @field:Json(name = "name") val name: String
)

data class ThumbnailDTO (
    @field:Json(name = "path") val path: String,
    @field:Json(name = "extension") val extension: String
)