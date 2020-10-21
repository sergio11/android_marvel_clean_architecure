package sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.mapper

import sanchez.sanchez.sergio.androidmobiletest.domain.models.Character
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.entity.CharacterEntity

/**
 * Mapper for Character Entity
 */
class CharacterDBMapper {

    /**
     * Model To Entity
     * @param model
     * @return entity
     */
    fun modelToEntity(model: Character): CharacterEntity =
        CharacterEntity(
            id = model.id,
            name = model.name,
            modified = model.modified,
            thumbnail = model.thumbnail,
            comics = model.comics,
            series = model.series,
            events = model.events
        )

    /**
     * Model List To Entity List
     * @param modelList
     * @return entityList
     */
    fun modelToEntity(modelList: List<Character>): List<CharacterEntity> =
        modelList.map {
            modelToEntity(it)
        }.toList()

    /**
     * Entity To Model
     * @param entity
     * @return model
     */
    fun entityToModel(entity: CharacterEntity): Character =
        Character(
            id = entity.id,
            name = entity.name,
            modified = entity.modified,
            thumbnail = entity.thumbnail,
            comics = entity.comics,
            series = entity.series,
            events = entity.events
        )


    fun entityToModel(entityList: List<CharacterEntity>): List<Character> =
        entityList.map {
            entityToModel(it)
        }.toList()

}