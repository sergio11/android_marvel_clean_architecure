package sanchez.sanchez.sergio.androidmobiletest.domain.models

/**
 * Character Domain Model
 */
data class Character (
    val id: Long,
    val name: String,
    val modified: String,
    val thumbnail: String,
    val comics: Long,
    val series: Long,
    val events: Long
)
