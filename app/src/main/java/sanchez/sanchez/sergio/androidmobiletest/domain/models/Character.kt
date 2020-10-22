package sanchez.sanchez.sergio.androidmobiletest.domain.models

import java.util.*

/**
 * Character Domain Model
 */
data class Character (
    val id: Long,
    val name: String,
    val modified: Date?,
    val thumbnail: String,
    val comics: Long,
    val series: Long,
    val events: Long
)
