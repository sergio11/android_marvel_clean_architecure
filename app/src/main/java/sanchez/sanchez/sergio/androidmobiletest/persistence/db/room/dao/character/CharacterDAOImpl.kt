package sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.dao.character

import androidx.room.Dao
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.dao.core.SupportDAO
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.entity.CharacterEntity

/**
 * Character DAO Impl
 */
@Dao
abstract class CharacterDAOImpl: SupportDAO<CharacterEntity>(), ICharacterDAO