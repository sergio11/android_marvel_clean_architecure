package sanchez.sanchez.sergio.androidmobiletest.domain.models

import java.util.*

/**
 * Character Detail Domain Model
 */

data class CharacterDetail (
    val id: Long,
    val name: String,
    val description: String,
    val modified: Date?,
    val thumbnail: String,
    val comics: Comics,
    val series: Comics,
    val events: Comics
)

data class Comics (
    val available: Long,
    val items: List<ComicsItem>,
    val returned: Long
)

data class ComicsItem (
    val name: String
)