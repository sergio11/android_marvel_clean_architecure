package sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.dao.character

import androidx.room.Query
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.dao.core.ISupportDAO
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.entity.CharacterEntity

/**
 * Characters DAO
 */
interface ICharacterDAO: ISupportDAO<CharacterEntity> {

    @Query("SELECT * FROM characters ORDER BY name DESC")
    suspend fun findAllOrderByNameDesc(): List<CharacterEntity>

    @Query("DELETE FROM characters")
    fun deleteAll()
}