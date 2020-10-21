package sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.core

import dagger.Module
import dagger.Provides
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.character.CharacterDBRepositoryImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.character.ICharacterDBRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.AppRoomDatabase
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.dao.character.ICharacterDAO
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.mapper.CharacterDBMapper

/**
 * Character DB Module
 */
@Module
class CharacterDBModule {

    /**
     * Provide Character DB Mapper
     */
    @Provides
    @PerFragment
    fun provideCharacterDBMapper(): CharacterDBMapper =
        CharacterDBMapper()

    /**
     * Provide Character DAO
     * @param database
     */
    @Provides
    @PerFragment
    fun provideCharacterDao(database: AppRoomDatabase): ICharacterDAO =
        database.characterDAO()

    /**
     * Provide Character DB Repository
     * @param characterDBMapper
     * @param characterDao
     */
    @Provides
    @PerFragment
    fun provideCharacterDBRepository(characterDBMapper: CharacterDBMapper, characterDao: ICharacterDAO): ICharacterDBRepository =
        CharacterDBRepositoryImpl(characterDao, characterDBMapper)
}