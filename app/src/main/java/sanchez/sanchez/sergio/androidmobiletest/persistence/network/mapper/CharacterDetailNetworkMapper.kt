package sanchez.sanchez.sergio.androidmobiletest.persistence.network.mapper

import sanchez.sanchez.sergio.androidmobiletest.domain.models.*
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.models.*
import java.lang.StringBuilder

/**
 * Character Network Mapper
 */
class CharacterDetailNetworkMapper {

    /**
     * DTO to Model
     * @param dto
     * @return model
     */
    fun dtoToModel(dto: CharacterDTO): CharacterDetail =
        CharacterDetail(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            modified = dto.modified,
            thumbnail = StringBuilder(dto.thumbnail.path)
                .append(".")
                .append(dto.thumbnail.extension)
                .toString(),
            comics = dtoToModel(dto.comics),
            series =  dtoToModel(dto.series),
            events =  dtoToModel(dto.events)
        )

    /**
     * DTO list to DTO model
     * @param dtoList
     * @return model list
     */
    fun dtoToModel(dtoList: List<CharacterDTO>): List<CharacterDetail> =
        dtoList.map {
            dtoToModel(it)
        }.toList()

    /**
     * Private Methods
     */

    private fun dtoToModel(dto: ComicsDTO): Comics =
        Comics(
            available = dto.available,
            returned = dto.returned,
            items = dto.items.map {
                dtoToModel(it)
            }.toList()
        )

    private fun dtoToModel(dto: ComicsItemDTO): ComicsItem =
        ComicsItem(dto.name)

}