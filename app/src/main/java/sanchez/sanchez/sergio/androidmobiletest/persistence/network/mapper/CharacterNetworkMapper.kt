package sanchez.sanchez.sergio.androidmobiletest.persistence.network.mapper

import sanchez.sanchez.sergio.androidmobiletest.domain.models.*
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.models.*
import java.lang.StringBuilder

/**
 * Character Network Mapper
 */
class CharacterNetworkMapper {

    /**
     * DTO to Model
     * @param dto
     */
    fun dtoToModel(dto: DataResult<CharacterDTO>): CharactersPage =
        CharactersPage(
            offset = dto.offset,
            limit = dto.limit,
            characterList = dtoToModel(dto.results)
        )

    /**
     * DTO to Model
     * @param dto
     * @return model
     */
    fun dtoToModel(dto: CharacterDTO): Character =
        Character(
            id = dto.id,
            name = dto.name,
            modified = dto.modified,
            thumbnail = StringBuilder(dto.thumbnail.path)
                .append(".")
                .append(dto.thumbnail.extension)
                .toString(),
            comics = dto.comics.returned,
            series =  dto.series.returned,
            events =  dto.events.returned
        )

    /**
     * DTO list to DTO model
     * @param dtoList
     * @return model list
     */
    fun dtoToModel(dtoList: List<CharacterDTO>): List<Character> =
        dtoList.map {
            dtoToModel(it)
        }.toList()

}